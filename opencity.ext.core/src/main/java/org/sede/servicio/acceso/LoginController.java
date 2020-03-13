package org.sede.servicio.acceso;


import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.OpenData;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.PermisosUser;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.AESSec;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.sede.core.validator.Recaptcha;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAO;
import org.sede.servicio.acceso.dao.GczUsuarioGenericDAO;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.Usuario;
import org.sede.servicio.acceso.userrequirements.RequirementsInterface;
import org.sede.servicio.padron.dao.PadronGenericDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

// TODO: Auto-generated Javadoc
/**
 * Class LoginController.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Controller
@RequestMapping(value = "/acceso", method = RequestMethod.GET)
@Gcz(servicio="ACCESO",seccion="REST")
public class LoginController {
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(LoginController.class);
	
	/** dao. */
	@Autowired
	private CiudadanoGenericDAO dao;
	
	/** acc. */
	@Autowired
	private GczUsuarioGenericDAO acc;
	
	/** dao padron. */
	@Autowired
	private PadronGenericDAO daoPadron;
	
	/** requirements. */
	@Autowired
	private Map<String, RequirementsInterface> requirements;
	
	
	
	/**
	 * Home.
	 *
	 * @param model Model
	 * @param r R
	 * @param t T
	 * @return string
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String home(Model model, 
			@RequestParam(name = "r", defaultValue = "") String r, 
			@RequestParam(name = "t", defaultValue = "") String t) {
		model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
		model.addAttribute("r", r);
		model.addAttribute("t", t);
		if (StringUtils.isEmpty(t)) {
			model.addAttribute(ModelAttr.DATO, new Ciudadano());
		}
		return "/login";
	}
	
	/**
	 * Salir.
	 *
	 * @param model Model
	 * @param r R
	 * @param request Request
	 * @param response Response
	 * @return string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value="/salir", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String salir(Model model,
			@RequestParam(name = "r", defaultValue = "") String r,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		HttpSession session = request.getSession();  
	    session.invalidate();
	    if ("".equals(r)) {
	    	return "redirect:/portal/datos-abiertos/";
	    } else {
	    	response.sendRedirect(r);
	    	return null;
	    }
	}
	
	/**
	 * Requirements.
	 *
	 * @param model Model
	 * @param r R
	 * @param request Request
	 * @param attr Attr
	 * @return string
	 */
	@NoCache
	@RequestMapping(value="/requirements", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String requirements(Model model,
			@RequestParam(name = "r", defaultValue = "") String r,
			HttpServletRequest request, RedirectAttributes attr) {
		model.addAttribute("r", r);
		Class<?>[] reqs = (Class[]) request.getSession().getAttribute(CheckeoParametros.REQUIREMENTESATTR);
		List<String> etiquetado = new ArrayList<String>();
		Ciudadano c = Funciones.getUser(request);
		for (int i = 0; i < reqs.length; i++) {
			Class<?> clase = reqs[i];
			RequirementsInterface req = requirements.get(clase.getSimpleName().toLowerCase());
			if (!req.validate(Funciones.getUser(request))) {
				String formElement = req.getFormElement(c, request); 
				if (formElement != null) {
					etiquetado.add(formElement);
				}
			}
		}
		model.addAttribute(ModelAttr.RESULTADO, etiquetado);
		
	    return "/requirements";
	}
	
	/**
	 * Save requirements.
	 *
	 * @param model Model
	 * @param redirigir Redirigir
	 * @param request Request
	 * @param response Response
	 * @param attr Attr
	 * @return string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value="/requirements/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String saveRequirements(Model model,
			@RequestParam(name = "r", defaultValue = "") String redirigir,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) throws IOException {
		model.addAttribute("r", redirigir);
		Class<?>[] reqs = (Class[]) request.getSession().getAttribute(CheckeoParametros.REQUIREMENTESATTR);
		Ciudadano c = Funciones.getUser(request);
		StringBuilder msg = new StringBuilder();
		for (int i = 0; i < reqs.length; i++) {
			Class<?> clase = reqs[i];
			RequirementsInterface req = requirements.get(clase.getSimpleName().toLowerCase());
			if (!req.validate(Funciones.getUser(request))) {
				String mensaje = req.storeRequirement(c, request); 
				if (!"".equals(mensaje)) {
					msg.append("<li>" + mensaje + "</li>");
				}
			}
			
		}
		if (msg.length() > 0) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "<ul>" + msg.toString() + "</ul>"));
			return requirements(model, redirigir, request, attr);
		} else {
			
			model.addAttribute(ModelAttr.MENSAJE, model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Datos actualizados correctamente")));
			request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, c);
			request.getSession().removeAttribute(CheckeoParametros.REQUIREMENTESATTR);
			response.sendRedirect(redirigir);
			return null;
		}
	    
	}
	
	/**
	 * Api login gcz OPTIONS.
	 *
	 * @param user User
	 * @param password Password
	 * @return response entity
	 */
	@ResponseClass(value = Credenciales.class)
	@RequestMapping(value="/credenciales", method = RequestMethod.OPTIONS, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Permisos(Permisos.DET)
    public @ResponseBody ResponseEntity<?> apiLoginGczOPTIONS(
    		@RequestParam(name = "user") String user, 
			@RequestParam(name = "password") String password) {
		return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Acceso habilitado"));
    }
	
	/**
	 * Api login gcz.
	 *
	 * @param user User
	 * @param password Password
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@ResponseClass(value = Credenciales.class)
	@RequestMapping(value="/credenciales", method = RequestMethod.POST, consumes = {MimeTypes.JSON, MimeTypes.XML}, produces = {MimeTypes.JSON, MimeTypes.XML})
	@OpenData
    public @ResponseBody ResponseEntity<?> apiLoginGcz(
    		@RequestParam(name = "user") String user, 
			@RequestParam(name = "password") String password) {
		if (Funciones.getPeticion().desdeOrigen()) {
			Credenciales credenciales = acc.getCredenciales(user, password);
			if (credenciales == null 
					|| credenciales.getUsuario().getLogin() == null 
					|| "".equals(credenciales.getUsuario().getLogin().trim())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso obteniendo credenciales"));
			} else {
				return ResponseEntity.ok(credenciales);
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
		}
    }

	/**
	 * Clone.
	 *
	 * @return object
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Api login ciudadano.
	 *
	 * @param email Email
	 * @param password Password
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@ResponseClass(value = Ciudadano.class)
	@RequestMapping(method = RequestMethod.POST, consumes = {MimeTypes.JSON, MimeTypes.XML}, produces = {MimeTypes.JSON, MimeTypes.XML})
	@Permisos(Permisos.ADMIN)
    public @ResponseBody ResponseEntity<?> apiLoginCiudadano(
    		@RequestParam(name = "email") String email, 
			@RequestParam(name = "password") String password) {
		if (email.trim().length() > 0 && password.trim().length() > 0) {
			Search busqueda = new Search(Ciudadano.class);
			busqueda.addFilter(Filter.equal("email", email.toLowerCase()));
			//busqueda.addFilter(Filter.equal("activado", "1"));
			busqueda.addFilter(Filter.equal("password", Funciones.calculateUserPassword(password)));
			Ciudadano registro = dao.searchUnique(busqueda);
			if (registro == null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso. Compruebe si los datos proporcionados son correctos."));
			} else if (registro.getActivado().intValue() == 1) {
				//Devolvemos un registro sólo con los datos necesarios para el acceso
				if ("Si".equals(registro.getEmpadronado())
						&& registro.getBirthYear() != null
						&& registro.getNif() != null
						&& (registro.getJunta() == null || registro.getDistrict() == null)) {
					try {
						String[] distritoSeccion = daoPadron.showDistrito(registro.getNif(), registro.getBirthYear()).split("-");
						registro.setDistrict(Integer.parseInt(distritoSeccion[0]));
						registro.setSection(Integer.parseInt(distritoSeccion[1]));
						registro.setJunta(daoPadron.showJunta(registro.getNif(), registro.getBirthYear()));
						dao.updatePadronData(registro);
					} catch (Exception e) {
						logger.error("Error al asociar datos de padron: " + e.getMessage());
					}
				}
				return ResponseEntity.ok(registro);
			} else {
				if(registro.getActivado().intValue() == 0){ // Usuario desactivado
					try {
						dao.sendActivationMail(registro); // Reenviamos email de confirmación
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Su usuario no está activado. Le hemos enviado un correo electrónico con el enlace de activación de la cuenta."));
				} else { // Usuarios desactivados manualmente u otros casos
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso."));
				}
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
		}
    }
	
	/**
	 * Api regenerar PK.
	 *
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@Permisos(Permisos.DET)
	@RequestMapping(value = "/regenerar-pk", method = RequestMethod.GET, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
    public ResponseEntity<?> apiRegenerarPK() {
		if (Funciones.getPeticion().desdeOrigen()) {
			String pk = RandomStringUtils.random(70, true, true);
			
			if (acc.setPk(Funciones.getPeticion().getClientId(), pk)) {
				Usuario usuario = new Usuario();
				usuario.setSecretKey(pk);
				if (usuario.getSecretKey() == null || "".equals((usuario.getSecretKey().trim()))) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
				} else {	
					return ResponseEntity.ok(usuario);
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "No se ha actualizado el registro"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
		}	
    }
	
	/**
	 * Api cambiar password.
	 *
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@Permisos(Permisos.DET)
	@RequestMapping(value = "/{id}/change-password", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
    public ResponseEntity<?> apiCambiarPassword() {
		if (Funciones.getPeticion().desdeOrigen()) {
			
			Usuario user = new Usuario();
			if (acc.update(Funciones.getPeticion().getClientId(), user, getContrasena())) {
				return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Contraseña modificada correctamente"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "No se ha actualizado el registro"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
		}	
    }
	
	/**
	 * Api crear.
	 *
	 * @param usuario Usuario
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@ResponseClass(value = Usuario.class)
	@Permisos(Permisos.NEW)
	@RequestMapping(value="/new", method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
    public @ResponseBody ResponseEntity<?> apiCrear(@RequestBody Usuario usuario) {
		if (Funciones.getPeticion().desdeOrigen() && usuario.getLogin() != null) {
			Set<ConstraintViolation<Object>> errores = acc.validar(usuario);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			}
			usuario.setSecretKey(RandomStringUtils.random(70, true, true));
			String mensaje = acc.crear(usuario, getContrasena());
			if ("".equals(mensaje)) {
				return ResponseEntity.ok(usuario);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(),
	    				mensaje));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(),
    				"Error en el acceso"));
		}	
    }
	
	/**
	 * Api modificar.
	 *
	 * @param id Id
	 * @param usuario Usuario
	 * @return response entity
	 */
	@Transactional(Constants.TM)
	@ResponseClass(value = Usuario.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
    public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable String id, @RequestBody Usuario usuario) {
		if (Funciones.getPeticion().desdeOrigen()) {
			
			if (acc.update(id, usuario, getContrasena())) {
				usuario.setLogin(id);
				return ResponseEntity.ok(usuario);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "No se ha actualizado el registro"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Error en el acceso"));
		}	
    }
	
	/**
	 * Login.
	 *
	 * @param model Model
	 * @param email Email
	 * @param user User
	 * @param password Password
	 * @param redirigir Redirigir
	 * @param t T
	 * @param gRecaptchaResponse G recaptcha response
	 * @param request Request
	 * @param response Response
	 * @return string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Transactional(Constants.TM)
	@RequestMapping(value="/login", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String login(Model model, 
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "user", defaultValue = "") String user,
			@RequestParam(name = "password") String password, 
			@RequestParam(name = "r", defaultValue = "") String redirigir, 
			@RequestParam(name = "t", defaultValue = "") String t,
			@RequestParam(name = "g-recaptcha-response", defaultValue = "") String gRecaptchaResponse,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		model.addAttribute("r", redirigir);
		model.addAttribute("t", t);
		if (Recaptcha.verify(gRecaptchaResponse, request)) {
			if ("".equals(t)) {
				ResponseEntity<?> respuesta = apiLoginCiudadano(email, password);
				if (respuesta.getStatusCode().equals(HttpStatus.OK)) {
					request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, respuesta.getBody());
				} else {
					model.addAttribute(ModelAttr.REGISTRO, respuesta);
					model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
					model.addAttribute(ModelAttr.DATO, new Ciudadano());
					return "/login";
				}
			} else {
				ResponseEntity<?> respuesta = apiLoginGcz(user, password);
				if (respuesta.getStatusCode().equals(HttpStatus.OK)) {
					request.getSession().setAttribute(CheckeoParametros.SESSIONGCZ, respuesta.getBody());
				} else {
					model.addAttribute(ModelAttr.REGISTRO, respuesta);
					model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
					model.addAttribute(ModelAttr.DATO, new Ciudadano());
					return "/login";
				}
			}
		} else {
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Mensaje(HttpStatus.FORBIDDEN.value(), "Debe marcar la casilla para indicar que no es un robot")));
			model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
			model.addAttribute(ModelAttr.DATO, new Ciudadano());
			return "/login";
		}
		response.sendRedirect(("".equals(redirigir) && "".equals(t)) ? "/opencityext/servicio/zona-personal/" : Funciones.getPathSecureWithoutContext() + redirigir);
		return null;
	}

	/**
	 * Recuperar.
	 *
	 * @param model Model
	 * @param email Email
	 * @param redirigir Redirigir
	 * @param t T
	 * @param gRecaptchaResponse G recaptcha response
	 * @param request Request
	 * @param response Response
	 * @return string
	 * @throws MessagingException the messaging exception
	 */
	@Transactional(Constants.TM)
	@RequestMapping(value="/recuperar", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String recuperar(Model model, 
			@RequestParam(name = "email", defaultValue = "") String email,
			@RequestParam(name = "r", defaultValue = "") String redirigir, 
			@RequestParam(name = "t", defaultValue = "") String t,
			@RequestParam(name = "g-recaptcha-response", defaultValue = "") String gRecaptchaResponse,
			HttpServletRequest request, HttpServletResponse response) throws MessagingException {
		model.addAttribute("r", redirigir);
		model.addAttribute("t", t);
		model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
		if ("".equals(email)) {
			return "/recuperar";
		} else {
			if (Recaptcha.verify(gRecaptchaResponse, request) && Funciones.isValidEmail(email)) {
				if ("".equals(t)) {
					if (StringUtils.isEmpty(t)) {
						model.addAttribute(ModelAttr.DATO, new Ciudadano());
					}
					String newPassword = dao.setNewPassword(email);
					if ("".equals(newPassword)) {
						model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha podido encontrar un usuario/a con el email introducido"));
					} else {
						Funciones.sendMail("Plataforma de Gobierno Abierto del Ayuntamiento de Zaragoza", 
							"<p>Has solicitado una nueva contraseña de acceso para la <a href=\"https://www.zaragoza.es/sede/acceso/\"><strong>Plataforma de Gobierno Abierto</strong></a> del Ayuntamiento de Zaragoza.</p>" +
							"<p>Tu nueva contraseña es: <strong>" + newPassword +"</strong></p><p>Te recomendamos cambiar tu contraseña una vez hayas accedido a la plataforma. Para ello ve al icono de la esquina superior derecha con tu nombre de usuario para que se abra un desplegable y accede a la opción de 'Cambiar Password'</p><p>Mire la siguiente imagen explicativa:</p><p><img src=\"http://www.zaragoza.es/cont/paginas/img/sede/zona-personal/cambiopasssede.jpg\" alt=\"proceso para cambiar el password\"/></p>"
							, email, "", "HTML");
						model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Se ha enviado un correo electrónico a la dirección indicada con la nueva contraseña"));
						model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
					}
					return "/login";	
				} else {
					// recuperar risp
					
					Mensaje msj = acc.enviarTokenRecuperacion(email);
					model.addAttribute(ModelAttr.MENSAJE, msj);
					if (msj.getStatus() == HttpStatus.OK.value()) {
						return "/login";
					} else {
						return "/recuperar";
					}
				}
			} else {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(),
	    				"Debe marcar la casilla de verificación e introducir una dirección de correo electrónico válida"));
				return "/recuperar";
			}
		}
	}
	
	/**
	 * Reset.
	 *
	 * @param model Model
	 * @param token Token
	 * @param pass Pass
	 * @param passRep Pass rep
	 * @param gRecaptchaResponse G recaptcha response
	 * @param request Request
	 * @param response Response
	 * @return string
	 */
	@Transactional(Constants.TM)
	@RequestMapping(value="/reset", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String reset(Model model, 
			@RequestParam(name = "token", defaultValue = "") String token,
			@RequestParam(name = "pass", defaultValue = "") String pass,
			@RequestParam(name = "pass_rep", defaultValue = "") String passRep,
			@RequestParam(name = "g-recaptcha-response", defaultValue = "") String gRecaptchaResponse,
			HttpServletRequest request, HttpServletResponse response) {
		
		model.addAttribute("token", token);
		model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
		
		if ("".equals(pass)) {
			String login = acc.obtenerLoginDeToken(token);
			if ("".equals(login)) {
				return "/reset";
			} else {
				model.addAttribute("login", login);
				return "/reset";
			}
		} else {
			if (Recaptcha.verify(gRecaptchaResponse, request)) {
				
				if (pass.equals(passRep)) {
					// cambiar password
					
					Peticion peticion = Funciones.getPeticion();
					peticion.setClientId(acc.obtenerLoginDeToken(token));
					peticion.setPassword(pass);
					request.setAttribute(CheckeoParametros.ATTRPETICION, peticion);
					
					ResponseEntity<?> resultado = this.apiCambiarPassword();
					if (resultado.getStatusCode().is2xxSuccessful()) {
						return "/reset-ok";
					} else {
						model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
						return "/reset";
					}
					
				} else {
					model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(),
		    				"Las contraseñas no coinciden"));
				}
				return "/reset";
			} else {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(),
	    				"Debe marcar la casilla de verificación"));
				return "/reset";
			}
		}
	}

	/**
	 * Crear.
	 *
	 * @param dato Dato
	 * @param pass Pass
	 * @param passRep Pass rep
	 * @param redirigir Redirigir
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param request Request
	 * @return string
	 */
	@Transactional(ConfigCiudadano.TM)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String crear(Ciudadano dato, @RequestParam(name = "pass", required = true) String pass,
						@RequestParam(name = "pass_rep", required = true) String passRep,
						@RequestParam(name = "r", defaultValue = "") String redirigir,
						BindingResult bindingResult, Model model, HttpServletRequest request) {
		model.addAttribute("r", redirigir);
		if (pass != null && pass.length() > 2 && pass.equals(passRep)) {
			Mensaje m = dao.crearUsuario(dato, pass);
			model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			if(m.getStatus() == HttpStatus.OK.value()) {
				model.addAttribute(ModelAttr.MENSAJE, m);
				return "/confirm-mail";
			} else {
				model.addAttribute("mensajeCrear", m);
				return "/login";
			}
		} else {
			model.addAttribute(ModelAttr.RECAPTCHA, Propiedades.getString("recaptcha.html"));
			model.addAttribute(ModelAttr.DATO, dato);
			model.addAttribute("mensajeCrear", new Mensaje(HttpStatus.BAD_REQUEST.value(), "Las contraseñas no coinciden. Por favor, vuelve a introduir la contraseña."));
			return "/login";
		}

	}
	
	/**
	 * Api crear ciudadano.
	 *
	 * @param ciudadano Ciudadano
	 * @param servicioRegistro Servicio registro
	 * @return response entity
	 */
	@Transactional(ConfigCiudadano.TM)
	@ResponseClass(value = Ciudadano.class)
	@Permisos(Permisos.NEW_USER)
	@RequestMapping(value="/save-api", method = RequestMethod.POST, consumes = { MimeTypes.JSON, MimeTypes.XML },
			produces = { MimeTypes.JSON, MimeTypes.XML })
    public @ResponseBody ResponseEntity<?> apiCrearCiudadano(@RequestBody Ciudadano ciudadano,
    		@RequestParam(value = "servicio", required = false) String servicioRegistro) {
		Mensaje m = dao.crearUsuario(ciudadano, ciudadano.getPassword());
		if (m.getStatus() == HttpStatus.OK.value()) {
//			if ((servicioRegistro != null) && ZGZ16Controller.SERVICIO.equalsIgnoreCase(servicioRegistro)) {
//		        String remitente = "Servicio de Juventud <juventud@zaragoza.es>";
//		        String asunto = "Plataforma de Gobierno Abierto: Validación en el programa Z16";
//				String mensaje = "<p>Te has registrado correctamente en el programa Z16, (a trav&eacute;s de la Plataforma de Gobierno Abierto).</p>" + 
//						"<p>Una vez hayas activado tu cuenta a trav&eacute;s del otro correo que habr&aacute;s recibido, debes VALIDAR TU INSCRIPCI&Oacute;N EN Z16 siguiendo las instrucciones que te indicamos a continuaci&oacute;n:</p>" + 
//						"<ol><li>En primer lugar, para un mejor aprovechamiento del programa, debes rellenar los datos de tu perfil, a los que puedes acceder desde el bot&oacute;n PERFIL en tu APP.</li>" + 
//						"<li>A continuaci&oacute;n, deber&aacute;s dirigirte al CIPAJ (enlace Web) o a la CASA DE JUVENTUD de tu barrio (enlace Web al Mapa Joven), donde se VALIDAR&Aacute; TU INSCRIPCI&Oacute;N.<br/>" + 
//						"Deber&aacute;s acudir con tu DNI o documento que acredite tu edad.</li></ol>" + 
//						"<p>Si tienes alguna duda, puedes consultar el TUTORIAL DE INCRIPCI&Oacute;N (enlace Web), o plante&aacute;rsela a la persona que te atienda cuando vayas a validar tu inscripci&oacute;n.</p>" + 
//						"<p>Tambi&eacute;n puedes contactar con el siguiente tel&eacute;fono: 888 888 888 888 (Horario 888 88 88 88 88 88).</p>";
//				try {
//					Funciones.sendMailCustomFrom(asunto, mensaje, remitente, ciudadano.getEmail(), "", "HTML");
//				} catch (MessagingException e) {
//					logger.error("Error al enviar correo instrucciones validación Z16 para el usuario: " + ciudadano.getEmail() + " - " + e.getMessage());
//				}
//			}
			return ResponseEntity.ok(ciudadano);
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
		}
	}
	
	/**
	 * Activate.
	 *
	 * @param email Email
	 * @param token Token
	 * @param model Model
	 * @param request Request
	 * @return string
	 * @throws InvalidKeyException the invalid key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws NoSuchPaddingException the no such padding exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 */
	@Transactional(ConfigCiudadano.TM)
	@RequestMapping(value = "/activate", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String activate(@RequestParam(name = "email", required = true) String email,
			@RequestParam(name = "token", required = true) String token,Model model, HttpServletRequest request) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		token=token.replace(" ", "+");
		String[] desen = AESSec.decrypt(token).split("#");
		boolean retorno = dao.activate(new BigDecimal(desen[0]), email, desen[1]);
		if (retorno) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Activación realizada correctamente."));
		} else {
			model.addAttribute(ModelAttr.MENSAJE,  new Mensaje(HttpStatus.BAD_REQUEST.value(),"No se ha podido realizar la activación"));
		}
		return "/activate";
	}


	/**
	 * Dis allow mail.
	 *
	 * @param model Model
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/disallow-mail", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String disAllowMail(Model model, RedirectAttributes attr, HttpServletRequest request) {
		Ciudadano c = Funciones.getUser(request);
		long resultado = dao.changeAllowMail(c, "N");		
		if (resultado > 0) {
			c.setAceptaMail(Boolean.FALSE);
			request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, c);
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "A partir de ahora dejará de recibir correos electrónicos relacionados con la Plataforma de Gobierno Abierto"));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha podido realizar la operacion."));
		}
		return "redirect:/servicio/zona-personal";
	}
	
	/**
	 * Lock.
	 *
	 * @param model Model
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/allow-mail", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String lock(Model model, RedirectAttributes attr, HttpServletRequest request) {
		Ciudadano c = Funciones.getUser(request);
		long resultado = dao.changeAllowMail(c, "S");		
		if (resultado > 0) {
			c.setAceptaMail(Boolean.TRUE);
			request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, c);
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "A partir de ahora podrá recibir correos electrónicos relacionados con la Plataforma de Gobierno Abierto"));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha podido realizar la operacion."));
		}
		return "redirect:/servicio/zona-personal";
	}
	
	/**
	 * Gets the contrasena.
	 *
	 * @return the contrasena
	 */
	private String getContrasena() {
		if (Funciones.getPeticion().getPassword() != null) {
			return Funciones.getPeticion().getPassword();
		}
		return "";
	}
	
}
