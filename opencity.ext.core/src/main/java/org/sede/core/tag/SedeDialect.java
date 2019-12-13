package org.sede.core.tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IProcessingContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionEnhancingDialect;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.IProcessor;
@Component
public class SedeDialect extends AbstractDialect implements IExpressionEnhancingDialect {

//	@Autowired
//	private AvisoTag aviso;
//	@Autowired
//	private TramiteTag tramite;
//	@Autowired
//	private NoticiaTag noticia;
//	@Autowired
//	private AgendaInstitucionalTag agendainstitucional;
//	@Autowired
//	private VoluntariadoTag voluntariado;
//	@Autowired
//	private AgendaTag agenda;
//	@Autowired
//	private NormativaTag normativa;
//	@Autowired
//	private EquipamientoTag equipamiento;
//	@Autowired
//	private AyuntamientoRespondeTag ayuntamientoResponde;
//	@Autowired
//	private FormularioTag formulario;
//	@Autowired
//	private CalendarioAgendaTag calendarioAgenda;
//	@Autowired
//	private VideoTag video;
//	@Autowired
//	private PropuestaTag propuesta;
	@Autowired
	private TemperaturaTag temperatura;
//	@Autowired
//	private PremiosConcursosTag premiosConcursos;
//	@Autowired
//	private OfertaEmpleoTag ofertaEmpleo;
//	@Autowired
//	private OrganigramaTag organigrama;
//	@Autowired
//	private TurismoPrimeraTag turismoPrimera;
//	@Autowired
//	private ContratoTag contrato;
//	@Autowired
//	private PreguntaFrecuenteTag preguntaFrecuente;
//	@Autowired
//	private MenuTag menu;

	public String getPrefix() {
		return "sede";
	}

	@Override
	  public Set<IProcessor> getProcessors() {
	    final Set<IProcessor> processors = new HashSet<IProcessor>();
	    processors.add(new FechaTag());
//	    processors.add(aviso);
//	    processors.add(tramite);
//	    processors.add(noticia);
//	    processors.add(agendainstitucional);
//		processors.add(voluntariado);
//	    processors.add(agenda);
//	    processors.add(normativa);
//		processors.add(calendarioAgenda);
//	    processors.add(equipamiento);
//	    processors.add(ayuntamientoResponde);
//	    processors.add(formulario);
//		processors.add(video);
//		processors.add(propuesta);
		processors.add(temperatura);
//		processors.add(premiosConcursos);
//		processors.add(ofertaEmpleo);
//		processors.add(organigrama);
//		processors.add(turismoPrimera);
//		processors.add(contrato);
//		processors.add(preguntaFrecuente);
//		processors.add(menu);
		processors.add(new SolrTag());
	    processors.add(new MetaTag());
	    processors.add(new BreadcrumbTag());
	    processors.add(new ContentTag());
	    processors.add(new RdfAttr());
	    processors.add(new ClassAttr());
	    processors.add(new CollapseTag());
	    return processors;
	  }

	public static Map<String, String> obtenerParametros(Element element, String attributeName) {
		final String attributeValue = element.getAttributeValue(attributeName);
		String[] parametros = attributeValue.split("#");
		HashMap<String, String> retorno = new HashMap<String, String>();
		for (String param:parametros) {
			String[] valor = param.split("=");
			retorno.put(valor[0].trim(), valor[1].trim());
		}
		return retorno;
	}
	@Override
	public Map<String, Object> getAdditionalExpressionObjects(
			IProcessingContext processingContext) {
		 final Map<String, Object> objects = new HashMap<String, Object>();
		 final Utils utils = new Utils();
	     objects.put("utils", utils);
	     return objects;
	}
}
