package org.sede.portal;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.blocking.BlockingCache;

import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/cache", method = RequestMethod.GET)
@Gcz(servicio="ADMIN",seccion="ADMIN")
public class CacheController {

	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE})
	@Permisos(Permisos.DET)
	public String home(Model model) {
		
		CacheManager manager = CacheManager.getInstance();
		
		HashMap<String, ArrayList<String>> listado = new HashMap<String, ArrayList<String>>();
		
		for (String key : manager.getCacheNames()) {
			ArrayList<String> claves = new ArrayList<String>();
			Ehcache cache = manager.getEhcache(key);
			if (cache != null) {
				
				if (!(cache instanceof BlockingCache)) {
                    // decorate and substitute
                    BlockingCache newBlockingCache = new BlockingCache(cache);
                    manager.replaceCacheWithDecoratedCache(cache,
                            newBlockingCache);
                }
				BlockingCache bc = (BlockingCache) manager.getEhcache(key);
				for (Object registro : bc.getKeys()) {
				    claves.add(registro.toString());
				}
			}
			listado.put(key, claves);
		}
		
		model.addAttribute("cache", listado);
		return "portal/cache";
	}
	
}
