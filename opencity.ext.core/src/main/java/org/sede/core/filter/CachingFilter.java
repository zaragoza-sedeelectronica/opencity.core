package org.sede.core.filter;

/**
 *  Copyright 2003-2009 Terracotta, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.DataFormatException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;
import net.sf.ehcache.constructs.web.AlreadyCommittedException;
import net.sf.ehcache.constructs.web.AlreadyGzippedException;
import net.sf.ehcache.constructs.web.GenericResponseWrapper;
import net.sf.ehcache.constructs.web.Header;
import net.sf.ehcache.constructs.web.PageInfo;
import net.sf.ehcache.constructs.web.ResponseHeadersNotModifiableException;
import net.sf.ehcache.constructs.web.ResponseUtil;
import net.sf.ehcache.constructs.web.SerializableCookie;
import net.sf.ehcache.constructs.web.filter.Filter;
import net.sf.ehcache.constructs.web.filter.FilterNonReentrantException;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sede.core.anotaciones.Cache;
import org.sede.core.plantilla.LayoutInterceptor;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.Rest;
import org.sede.core.tag.Utils;
import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.analytics.AsyncLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * An abstract CachingFilter.
 * <p/>
 * This class should be sub-classed for each page to be cached.
 * <p/>
 * The filters must be declared in the web.xml deployment descriptor. Then a
 * mapping from a web resource, such as a JSP Page, FreeMarker page, Velocity
 * page, Servlet or static resouce needs to be defined. Finally, a succession of
 * mappings can be used to create a filter chain. See SRV.6 of the Servlet 2.3
 * specification for more details.
 * <p/>
 * Care should be taken not to define a filter chain such that the same
 * {@link CachingFilter} class is reentered. The {@link CachingFilter} uses the
 * {@link net.sf.ehcache.constructs.blocking.BlockingCache}. It blocks until the
 * thread which did a get which results in a null does a put. If reentry happens
 * a second get happens before the first put. The second get could wait
 * indefinitely. This situation is monitored and if it happens, an
 * IllegalStateException will be thrown.
 * <p/>
 * The following init-params are supported:
 * <ol>
 * <li>cacheName - the name in ehcache.xml used by the filter.
 * <li>blockingTimeoutMillis - the time, in milliseconds, to wait for the filter
 * chain to return with a response on a cache miss. This is useful to fail fast
 * in the event of an infrastructure failure.
 * </ol>
 * 
 * @author @author Greg Luck
 */
public abstract class CachingFilter extends Filter {

	private static final Logger logger = LoggerFactory
			.getLogger(CachingFilter.class);
    private static final String BLOCKING_TIMEOUT_MILLIS = "blockingTimeoutMillis";
    

    /**
     * The cache holding the web pages. Ensure that all threads for a given
     * cache name are using the same instance of this.
     */
    protected HashMap<String, BlockingCache> blockingCache;

    private final VisitLog visitLog = new VisitLog();

    /**
     * Initialises blockingCache to use. The BlockingCache created by this
     * method does not have a lock timeout set.
     * <p/>
     * A timeout can be appled using
     * <code>blockingCache.setTimeoutMillis(int timeout)</code> and takes effect
     * immediately for all new requests
     * 
     * @throws CacheException
     *             The most likely cause is that a cache has not been configured
     *             in ehcache's configuration file ehcache.xml for the filter
     *             name
     * @param filterConfig
     *            this filter's configuration.
     */
    public void doInit(FilterConfig filterConfig) {
        synchronized (this.getClass()) {
            if (blockingCache == null) {
            	blockingCache = new HashMap<String, BlockingCache>();
                CacheManager manager = CacheManager.getInstance();
        		for (String key : manager.getCacheNames()) {
        			BlockingCache bc;
                    Ehcache cache = getCacheManager().getEhcache(key);
                    if (cache == null) {
                        throw new CacheException("cache '" + key
                                + "' not found in configuration");
                    }
                    if (!(cache instanceof BlockingCache)) {
                        // decorate and substitute
                        BlockingCache newBlockingCache = new BlockingCache(cache);
                        getCacheManager().replaceCacheWithDecoratedCache(cache,
                                newBlockingCache);
                    }
                    bc = (BlockingCache) getCacheManager().getEhcache(key);
                    Integer blockingTimeoutMillis = parseBlockingCacheTimeoutMillis(filterConfig);
                    if (blockingTimeoutMillis != null && blockingTimeoutMillis > 0) {
                    	bc.setTimeoutMillis(blockingTimeoutMillis);
                    }
                    blockingCache.put(key, bc);
        		}
            }
        }
    }

    /**
     * Reads the filterConfig for the parameter "blockingTimeoutMillis", and if
     * found, set the blocking timeout. If there is a parsing exception, no
     * timeout is set.
     */
    Integer parseBlockingCacheTimeoutMillis(FilterConfig filterConfig) {

        String timeout = filterConfig.getInitParameter(BLOCKING_TIMEOUT_MILLIS);
        try {
            return Integer.parseInt(timeout);
        } catch (NumberFormatException e) {
            return null;
        }

    }

    /**
     * Destroys the filter.
     */
    protected void doDestroy() {
        // noop
    }

    /**
     * Performs the filtering for a request. This method caches based responses
     * keyed by {@link #calculateKey(javax.servlet.http.HttpServletRequest)}
     * <p/>
     * By default this method will queue requests requesting the page response
     * for a given key until the first thread in the queue has completed. The
     * request which occurs when the page expires incurs the cost of waiting for
     * the downstream processing to return the respone.
     * <p/>
     * The maximum time to wait can be configured by setting
     * <code>setTimeoutMillis</code> on the underlying
     * <code>BlockingCache</code>.
     * 
     * @param request
     * @param response
     * @param chain
     * @throws AlreadyGzippedException
     *             if a double gzip is attempted
     * @throws AlreadyCommittedException
     *             if the response was committed on the way in or the on the way
     *             back
     * @throws FilterNonReentrantException
     *             if an attempt is made to reenter this filter in the same
     *             request.
     * @throws LockTimeoutException
     *             if this request is waiting on another that is populating the
     *             cache entry and timeouts while waiting. Only occurs if the
     *             BlockingCache has a timeout set.
     * @throws Exception
     *             for all other exceptions. They will be caught and logged in
     *             {@link Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
     */
    protected void doFilter(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws Exception {
        if (response.isCommitted()) {
            throw new AlreadyCommittedException(
                    "Response already committed before doing buildPage.");
        }
        if (request.getMethod().equals("OPTIONS")){
        	response.setContentType(MimeTypes.JSON);
    		response.setHeader("Allow", Rest.METHODS);
    		response.setHeader("Access-Control-Allow-Origin", "*");
    		response.setHeader("Access-Control-Allow-Methods", Rest.METHODS);
    		response.setHeader("Access-Control-Allow-Headers", CheckeoParametros.CONTENT_TYPE + "," + CheckeoParametros.ACCEPTHEADER +  "," +CheckeoParametros.HEADERPASSWORD + "," + CheckeoParametros.HEADERCLIENTID + "," + CheckeoParametros.HEADERHMAC);
    		response.getOutputStream().print("{\"status\":" + HttpStatus.ACCEPTED.value() + "}");
        } else {
	        logRequestHeaders(request);
	        if (request.getParameter(CheckeoParametros.DEBUG) != null) {
            	logger.error("Antes ejecutar");
            }
	        PageInfo pageInfo = buildPageInfo(request, response, chain);
	        if (request.getParameter(CheckeoParametros.DEBUG) != null) {
            	logger.error("despues de ejecutar");
            }
	        if (pageInfo != null && (pageInfo.getContentType() != null || pageInfo.isOk())) {
	            if (response.isCommitted()) {
	                throw new AlreadyCommittedException(
	                        "Response already committed after doing buildPage"
	                                + " but before writing response from PageInfo.");
	            }
	            if (request.getParameter(CheckeoParametros.DEBUG) != null) {
	            	logger.error("Antes de escritura");
	            }
	            writeResponse(request, response, pageInfo);
	            if (request.getParameter(CheckeoParametros.DEBUG) != null) {
	            	logger.error("despues de escritura");
	            }
	        } else {
	        	OutputStream out = new BufferedOutputStream(response.getOutputStream());
	            out.write(getPaginaError(request, response).getBytes());
	            out.flush();
	        }
        }
    }

    private String getPaginaError(HttpServletRequest request, HttpServletResponse response) {
    	String edit = null;
    
    	try {
    		edit = (Utils.editContent(((Peticion)request.getAttribute(CheckeoParametros.ATTRPETICION)).getCredenciales(), (String)request.getAttribute(LayoutInterceptor.PLANTILLA_ATTR), (String)request.getAttribute(LayoutInterceptor.VIEW_ATTR)));
    	} catch (Exception e) {
    		;
    	}
    	response.setStatus(HttpStatus.BAD_REQUEST.value());
        return "<!DOCTYPE html>"
            + "<html lang=\"es\">"
            + "<head>"
            + " <meta charset=\"utf-8\" />"
            + " <title>Página de error</title>"
            + " <link rel=\"stylesheet\" href=\"//www.zaragoza.es/cont/plantillas/sede/css/main.min.css\" />"
            + " <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css\" />"
            + "</head>"
            + "<body>"
            + "<div id=\"ayto\">"
            + " <header class=\"navbar navbar-default navbar-fixed-top navbar-portal\">"
            + "  <div class=\"container-fluid\">"
            + "   <a class=\"logo pull-left\" href=\"http://www.zaragoza.es\" title=\"Ir a la página principal de la Sede Electrónica del Ayuntamiento de Zaragoza\">"
            + "    <img src=\"//www.zaragoza.es/cont/paginas/img/sede/logo_escudo.png\" alt=\"Logotipo Ayuntamiento de Zaragoza\">"
            + "   </a>"
            + "   <div class=\"pull-left portal-header\">"
            + "    <h1><a href=\"http://www.zaragoza.es\">Ayuntamiento de Zaragoza</a></h1>"
            + "    <nav><ul class=\"breadcrumb small\" vocab=\"http://rdf.data-vocabulary.org/\"><li><span typeof=\"v:Breadcrumb\"><a href=\"http://www.zaragoza.es\" rel=\"v:url\" property=\"v:title\">zaragoza.es</a></span></li></ul></nav>"
            + "   </div>"
            + (edit == null ? "" : edit)
            + "  </div>"
            + " </header>"
            + " <div class=\"container-fluid\">"
            + "  <div class=\"alert alert-danger\">"
            + "   <p>Se produjo un error al mostrar la página disculpe las molestias</p>"
            + "   <p>Puede comunicárnoslo utilizando el <a href=\"/ciudad/ticketing/verNuevaQuejaAnonima_Ticketing\"><strong>Servicio de Quejas y Sugerencias</strong></a></p>"
            + "  </div>"
            + (request.getParameter(CheckeoParametros.DEBUG) != null && request.getAttribute(CheckeoParametros.TM_ERROR) != null 
            	? "<div class=\"alert alert-warning\">" 
            		+ request.getAttribute(CheckeoParametros.TM_ERROR)
            		 +  "<pre>" + request.getAttribute(CheckeoParametros.TM_ERROR_STACK) + "</pre>"
            	+ "</div>": "")
            + " </div>"
            + " <footer class=\"fnd-gris-claro margin-t3em\">"
            + "  <div class=\"container-fluid text-center\">"
            + "   <a class=\"navbar-left\" href=\"/opencityext/portal/copy\">"
            + "    <strong>&copy; Ayuntamiento de Zaragoza. " + Calendar.getInstance().get(Calendar.YEAR) + "</strong>"
            + "   </a>"
            + "  </div>"
            + " </footer>"
            + (edit == null ? "" : "<script src=\"//www.zaragoza.es/cont/plantillas/js/jquery-1.11.3.min.js\"></script>"
            		+ "<script src=\"//www.zaragoza.es/cont/plantillas/bs/js/bootstrap3.min.js\"></script>"
            		+ "<script src=\"https://www.zaragoza.es/cont/plantillas/sede/js/main.js\"></script>")
            + "</div>"
            + "</body>"
            + "</html>";
	}

	/**
     * Build page info either using the cache or building the page directly.
     * <p/>
     * Some requests are for page fragments which should never be gzipped, or
     * for other pages which are not gzipped.
     */
    protected PageInfo buildPageInfo(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws Exception {
        // Look up the cached page
        final String key = calculateKey(request);
        PageInfo pageInfo = null;
        try {
            checkNoReentry(request);
            if (request.getParameter(CheckeoParametros.REFRESHPARAMETER) != null) {
            	for (Map.Entry<String, BlockingCache> entry : blockingCache.entrySet()) {
                	if (entry.getValue().isKeyInCache(key)) {
                		String formato;
                		try {
                			formato = InterceptorPeticion.obtenerFormato(request.getRequestURI(), request.getHeader(CheckeoParametros.ACCEPTHEADER)).getExtension();
                		} catch (Exception e) {
                			formato = "htm"; 
                		}
                        final String keyRefresh = GZipFilter.calcularClaveCache(request, formato);
                		
                		
                		entry.getValue().remove(keyRefresh);
                	}
                }
            }
            Element element = null;
            
            for (Map.Entry<String, BlockingCache> entry : blockingCache.entrySet()) {
            	if (entry.getValue().isKeyInCache(key)) {
                    element = entry.getValue().getQuiet(key);
            	}
            }
            if (element == null || element.getObjectValue() == null || request.getParameter(CheckeoParametros.REFRESHPARAMETER) != null) {
                try {
                	// Page is not cached - build the response, cache it, and
                    // send to client
                    pageInfo = buildPage(request, response, chain);
                    String actualCache = request.getAttribute(CheckeoParametros.ACTUAL_CACHE) == null ? Cache.DEFAULT_CACHE_NAME : (String)request.getAttribute(CheckeoParametros.ACTUAL_CACHE);
                    if (pageInfo.isOk()) {
                        if (request.getParameter(CheckeoParametros.DEBUG) != null) {
                            logger.debug("PageInfo ok. Adding to cache {} with key {}",blockingCache.get(actualCache).getName(), key);
                        }
                        if ("GET".equalsIgnoreCase(request.getMethod()) 
                        		&& (request.getAttribute(CheckeoParametros.NOCACHE) != null 
                        		&& ((Boolean)request.getAttribute(CheckeoParametros.NOCACHE)).booleanValue() == true)) {
                        	blockingCache.get(actualCache).put(new Element(key, pageInfo));
                        } else {
                        	logger.info("Contenido no cacheado al no estar incluida la anotación @Cache o requerir permiso para su ejecucion");
                        	blockingCache.get(actualCache).remove(key);
                        }
                    } else {
                        if (request.getParameter(CheckeoParametros.DEBUG) != null) {
                            logger.debug("PageInfo was not ok(200). Putting null into cache {} with key {}", blockingCache.get(actualCache).getName(), key);
                        }
                        blockingCache.get(actualCache).remove(key);
                    }
                } catch (final Throwable throwable) {
                    // Must unlock the cache if the above fails. Will be logged
                    // at Filter
                	for (Map.Entry<String, BlockingCache> entry : blockingCache.entrySet()) {
                		blockingCache.get(entry.getKey()).remove(key);
                    }
                	
                    return null;
                    
                }
            } else {
                pageInfo = (PageInfo) element.getObjectValue();
            }
        } catch (LockTimeoutException e) {
            // do not release the lock, because you never acquired it
            throw e;
        } finally {
            // all done building page, reset the re-entrant flag
            visitLog.clear();
        }
        return pageInfo;
    }

    /**
     * Builds the PageInfo object by passing the request along the filter chain
     * 
     * @param request
     * @param response
     * @param chain
     * @return a Serializable value object for the page or page fragment
     * @throws ServletException 
     * @throws IOException 
     * @throws AlreadyGzippedException
     *             if an attempt is made to double gzip the body
     * @throws Exception
     */
    protected PageInfo buildPage(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain) throws IOException, ServletException {
        // Invoke the next entity in the chain
        final ByteArrayOutputStream outstr = new ByteArrayOutputStream();
        final GenericResponseWrapper wrapper = new GenericResponseWrapper(
                response, outstr);
        
        if (ServletFileUpload.isMultipartContent(request)) {
        	chain.doFilter(request, wrapper);	
        } else {
        	chain.doFilter(new RequestWrapper(request), wrapper);
        }
        
        wrapper.flush();
        String actualCache = request.getAttribute(CheckeoParametros.ACTUAL_CACHE) == null ? Cache.DEFAULT_CACHE_NAME : (String)request.getAttribute(CheckeoParametros.ACTUAL_CACHE);
        long timeToLiveSeconds = blockingCache.get(actualCache).getCacheConfiguration()
                .getTimeToLiveSeconds();

        // Return the page info
        return new PageInfo(wrapper.getStatus(), wrapper.getContentType(),
                wrapper.getCookies(), outstr.toByteArray(), true,
                timeToLiveSeconds, wrapper.getAllHeaders());
    }

    /**
     * Writes the response from a PageInfo object.
     * <p/>
     * Headers are set last so that there is an opportunity to override
     * 
     * @param request
     * @param response
     * @param pageInfo
     * @throws IOException
     * @throws DataFormatException
     * @throws ResponseHeadersNotModifiableException
     * 
     */
    protected void writeResponse(final HttpServletRequest request,
            final HttpServletResponse response, final PageInfo pageInfo)
            throws IOException {
        boolean requestAcceptsGzipEncoding = acceptsGzipEncoding(request);

        setStatus(response, pageInfo);
        setContentType(response, pageInfo);
        setCookies(pageInfo, response);
        // do headers last so that users can override with their own header sets
        setHeaders(pageInfo, requestAcceptsGzipEncoding, response);
        writeContent(request, response, pageInfo);
        
        String formato;
		try {
			formato = InterceptorPeticion.obtenerFormato(request.getRequestURI(), request.getHeader(CheckeoParametros.ACCEPTHEADER)).getExtension();
		} catch (Exception e) {
			formato = "htm"; 
		}
		if (!"htm".equals(formato)) {
			String clientId = request.getHeader(CheckeoParametros.HEADERCLIENTID);
			if (clientId == null && request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null) {
				clientId = ((Credenciales)request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ)).getUsuario().getLogin();
			}
			String uri = request.getRequestURI().indexOf('.') > 0 ? request.getRequestURI() : request.getRequestURI() + "." + formato;
			if (request.getQueryString() != null) {
				uri = uri + "?" + request.getQueryString();
			}
			String error = null;
			if (pageInfo.getStatusCode() != 200) {
				error = "" + pageInfo.getStatusCode();
			}
			if (request.getParameter(CheckeoParametros.DEBUG) != null) {
				logger.error("Peticion api: " + clientId +":"+ Funciones.getIpUser(request) +":"+ request.getMethod()+":"+ uri +":"+ request.getHeader(CheckeoParametros.USERAGENT)+":"+request.getHeader(CheckeoParametros.REFERER)+":"+error);
			}
	        AsyncLog.log(clientId, Funciones.getIpUser(request), request.getMethod(), uri, formato, request.getHeader(CheckeoParametros.USERAGENT), request.getHeader(CheckeoParametros.REFERER), error);
		}
    }

    /**
     * Set the content type.
     * 
     * @param response
     * @param pageInfo
     */
    protected void setContentType(final HttpServletResponse response,
            final PageInfo pageInfo) {
        String contentType = pageInfo.getContentType();
        if (contentType != null && contentType.length() > 0) {
            response.setContentType(contentType);
        }
    }

    /**
     * Set the serializableCookies
     * 
     * @param pageInfo
     * @param response
     */
    @SuppressWarnings("rawtypes")
	protected void setCookies(final PageInfo pageInfo,
            final HttpServletResponse response) {

        final Collection cookies = pageInfo.getSerializableCookies();
        for (Iterator iterator = cookies.iterator(); iterator.hasNext();) {
            final Cookie cookie = ((SerializableCookie) iterator.next())
                    .toCookie();
            response.addCookie(cookie);
        }
    }

    /**
     * Status code
     * 
     * @param response
     * @param pageInfo
     */
    protected void setStatus(final HttpServletResponse response,
            final PageInfo pageInfo) {
        response.setStatus(pageInfo.getStatusCode());
    }

    /**
     * Set the headers in the response object, excluding the Gzip header
     * 
     * @param pageInfo
     * @param requestAcceptsGzipEncoding
     * @param response
     */
    protected void setHeaders(final PageInfo pageInfo,
            boolean requestAcceptsGzipEncoding,
            final HttpServletResponse response) {

        final Collection<Header<? extends Serializable>> headers = pageInfo
                .getHeaders();

        // Track which headers have been set so all headers of the same name
        // after the first are added
        final TreeSet<String> setHeaders = new TreeSet<String>(
                String.CASE_INSENSITIVE_ORDER);
        response.setHeader("X-UA-Compatible", "IE=edge");
		response.setHeader("Allow", Rest.METHODS);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", Rest.METHODS);
        response.setHeader("Access-Control-Allow-Headers", CheckeoParametros.CONTENT_TYPE + "," + CheckeoParametros.ACCEPTHEADER +  "," +CheckeoParametros.HEADERPASSWORD + "," + CheckeoParametros.HEADERCLIENTID + "," + CheckeoParametros.HEADERHMAC);
        
        
        for (final Header<? extends Serializable> header : headers) {
            final String name = header.getName();

            switch (header.getType()) {
            case STRING:
                if (setHeaders.contains(name)) {
                    response.addHeader(name, (String) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setHeader(name, (String) header.getValue());
                }
                break;
            case DATE:
                if (setHeaders.contains(name)) {
                    response.addDateHeader(name, (Long) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setDateHeader(name, (Long) header.getValue());
                }
                break;
            case INT:
                if (setHeaders.contains(name)) {
                    response.addIntHeader(name, (Integer) header.getValue());
                } else {
                    setHeaders.add(name);
                    response.setIntHeader(name, (Integer) header.getValue());
                }
                break;
            default:
                throw new IllegalArgumentException("No mapping for Header: "
                        + header);
            }
        }
    }

    /**
     * Gets the CacheManager for this CachingFilter. It is therefore up to
     * subclasses what CacheManager to use.
     * <p/>
     * This method was introduced in ehcache 1.2.1. Older versions used a
     * singleton CacheManager instance created with the default factory method.
     * 
     * @return the CacheManager to be used
     * @since 1.2.1
     */
    protected abstract CacheManager getCacheManager();

    /**
     * CachingFilter works off a key.
     * <p/>
     * The key should be unique. Factors to consider in generating a key are:
     * <ul>
     * <li>The various hostnames that a request could come through
     * <li>Whether additional parameters used for referral tracking e.g. google
     * should be excluded to maximise cache hits
     * <li>Additional parameters can be added to any page. The page will still
     * work but will miss the cache. Consider coding defensively around this
     * issue.
     * </ul>
     * <p/>
     * Implementers should differentiate between GET and HEAD requests otherwise
     * blank pages can result. See SimplePageCachingFilter for an example
     * implementation.
     * 
     * @param httpRequest
     * @return the key, generally the URL plus request parameters
     */
    protected abstract String calculateKey(final HttpServletRequest httpRequest);

    /**
     * Writes the response content. This will be gzipped or non gzipped
     * depending on whether the User Agent accepts GZIP encoding.
     * <p/>
     * If the body is written gzipped a gzip header is added.
     * 
     * @param response
     * @param pageInfo
     * @throws IOException
     */
    protected void writeContent(final HttpServletRequest request,
            final HttpServletResponse response, final PageInfo pageInfo)
            throws IOException {
        byte[] body;

        boolean shouldBodyBeZero = ResponseUtil.shouldBodyBeZero(request,
                pageInfo.getStatusCode());
        if (shouldBodyBeZero) {
            body = new byte[0];
        } else if (acceptsGzipEncoding(request)) {
            body = pageInfo.getGzippedBody();
            if (ResponseUtil.shouldGzippedBodyBeZero(body, request)) {
                body = new byte[0];
            } else {
                ResponseUtil.addGzipHeader(response);
            }

        } else {
            body = pageInfo.getUngzippedBody();
        }

        response.setContentLength(body.length);
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(body);
        out.flush();
    }

    /**
     * Check that this caching filter is not being reentered by the same
     * recursively. Recursive calls will block indefinitely because the first
     * request has not yet unblocked the cache.
     * <p/>
     * This condition usually indicates an error in filter chaining or
     * RequestDispatcher dispatching.
     * 
     * @param httpRequest
     * @throws FilterNonReentrantException
     *             if reentry is detected
     */
    protected void checkNoReentry(final HttpServletRequest httpRequest) {
        String filterName = getClass().getName();
        if (visitLog.hasVisited()) {
            throw new FilterNonReentrantException(
                    "The request thread is attempting to reenter" + " filter "
                            + filterName + ". URL: "
                            + httpRequest.getRequestURL());
        } else {
            // mark this thread as already visited
            visitLog.markAsVisited();
            if (logger.isDebugEnabled()) {
                logger.debug("Thread {}  has been marked as visited.", Thread
                        .currentThread().getName());
            }
        }
    }

    /**
     * threadlocal class to check for reentry
     * 
     * @author hhuynh
     * 
     */
    private static class VisitLog extends ThreadLocal<Boolean> {
        @Override
        protected Boolean initialValue() {
            return false;
        }

        public boolean hasVisited() {
            return get();
        }

        public void markAsVisited() {
            set(true);
        }

        public void clear() {
            super.remove();
        }
    }
}
