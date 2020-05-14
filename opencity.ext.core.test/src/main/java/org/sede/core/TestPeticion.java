package org.sede.core;
import org.mockito.InjectMocks;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Hmac;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
public class TestPeticion {
	@InjectMocks
	private MockMvc mockMvc;
	
	private String clientId;
	private String clavePrivada;



	public TestPeticion() {
		clientId = null;
		clavePrivada = null;
	
	}
	public void setMock(MockMvc moc) {
		mockMvc = moc;
		
	}
	

	public TestPeticion(String clientId, String clavePrivada) {
		super();
		this.clientId = clientId;
		this.clavePrivada = clavePrivada;
	}
	public ResultActions get(String uri) {
		return get(uri, MediaType.APPLICATION_JSON);
	}
	public ResultActions get(String uri, MediaType accept) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(uri)
					.headers(setHeaders(uri, null, RequestMethod.GET))
					.accept(accept)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public ResultActions get(String uri, MockHttpSession session) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(uri)
					.headers(setHeaders(uri, null, RequestMethod.GET))
					//.accept(accept)
					.session(session)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public ResultActions options(String uri) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get(uri)
					.headers(setHeaders(uri, null, RequestMethod.OPTIONS))
					.accept(MediaType.ALL)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public ResultActions delete(String uri) {
		return delete(uri, MediaType.APPLICATION_JSON);
	}
	public ResultActions delete(String uri, MediaType accept) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.delete(uri)
					.headers(setHeaders(uri, null, RequestMethod.DELETE))
					.accept(accept)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public ResultActions post(String uri, String body) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
					.headers(setHeaders(uri, body, RequestMethod.POST))
					.accept(MediaType.APPLICATION_JSON)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ResultActions post(String uri, String body, MockHttpSession session) {
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
					.headers(setHeaders(uri, body, RequestMethod.POST))
					.accept(MediaType.APPLICATION_JSON)
					.session(session)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public ResultActions put(String uri, String body) {
		
		try {
			ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.put(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.content(body)
					.headers(setHeaders(uri, body, RequestMethod.PUT))
					.accept(MediaType.APPLICATION_JSON)
					);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public HttpHeaders setHeaders(String uri, String body,
			RequestMethod metodoHttp) {
		HttpHeaders headers = new HttpHeaders();
		String hmac = "";
		if (clientId != null) {
			System.out.println(clientId + metodoHttp + uri + (body == null ? "" : body));
			hmac = Hmac.calcular(clientId + metodoHttp + uri + (body == null ? "" : body), clavePrivada);
			headers.add(CheckeoParametros.HEADERCLIENTID, this.clientId);
			headers.add(CheckeoParametros.HEADERHMAC, hmac);
		}

		return headers;
	}
	public void setCredenciales() {
		this.clientId = PropiedadesTest.getClientID();
		this.clavePrivada = PropiedadesTest.getPK();
	}


}
