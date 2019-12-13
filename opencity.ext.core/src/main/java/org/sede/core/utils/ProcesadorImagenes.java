package org.sede.core.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/** Clase que implementa un procesador para imagenes y juguetear con ellas */
public class ProcesadorImagenes {
	private static final Logger logger = LoggerFactory.getLogger(ProcesadorImagenes.class);
	/** Opciones de renderizado para las imagenes */ 
	private RenderingHints opciones = new RenderingHints(null);
 
	/** Constructor de la clase */
	public ProcesadorImagenes() {
 
		// Cargo las opciones de renderizado que me apetezcan	
		opciones.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		opciones.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		opciones.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
		opciones.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		opciones.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		opciones.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		opciones.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		opciones.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
 
	/** Devuelve la lista de formatos disponibles a leer por ImageIO	
	 * @return un array de strings con los mismos.	
	 */
	public String[] dameFormatosUsables(){
 
		return ImageIO.getReaderFormatNames();
	}
 
 
	/** Calcula el factor de escala minimo y en base a eso 
	 * escala la imagen segun dicho factor.	
	* @param nMaxWidth maximo tamanno para el ancho
	* @param nMaxHeight nmaximo tamanno para el alto
	* @param imagen Imagen que vamos a escalar
	* @return Devuelve la imagen escalada para poderla trastocar o null si hay error
	*/
	public BufferedImage escalarATamanyo(final BufferedImage imagen,
			final int maximoAncho, final int maximoAlto) {
 
		// Comprobacion de parametros
		if (imagen == null || maximoAlto == 0 || maximoAncho == 0) {
			return null;
		}
 
		// Capturo ancho y alto de la imagen 
		int anchoImagen = imagen.getWidth();
		int altoImagen = imagen.getHeight();
		// Calculo la relacion entre anchos y altos de la imagen
		double escalaX = (double)maximoAncho / (double)anchoImagen;
		double escalaY = (double)maximoAlto / (double)altoImagen;
		// Tomo como referencia el minimo de las escalas
		double fEscala = Math.min(escalaX, escalaY);
		// Devuelvo el resultado de aplicar esa escala a la imagen
		return escalar(fEscala, imagen);
	}
 
	
 
	public BufferedImage resize(final BufferedImage img,
			final int ancho, final int alto) {
		int w = img.getWidth();  
		int h = img.getHeight();  
		BufferedImage dimg = new BufferedImage(ancho, alto, img.getType());  
		Graphics2D g = dimg.createGraphics();  
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
		g.drawImage(img, 0, 0, ancho, alto, 0, 0, w, h, null);  
		g.dispose();
		return dimg;  
	}
	
	/** Escala una imagen en porcentaje.
	* @param factorEscala ejemplo: factorEscala=0.6 (escala la imagen al 60%)
	* @param srcImg una imagen BufferedImage
	* @return un BufferedImage escalado
	*/
	public BufferedImage escalar(final double factorEscala, final BufferedImage srcImg) {
 
		// Comprobacion de parametros
		if (srcImg == null) {
			return null;
		}
 
		// Compruebo escala nula
		if (factorEscala == 1 ) {
 
			return srcImg;
		}
 
		// La creo con esas opciones
		AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(factorEscala, factorEscala), opciones);
		
		// Devuelve el resultado de aplicar el filro sobre la imagen
		return op.filter(srcImg, null);
	}
 
 
	/** Metodo que guarda una imagen en disco 
	 * @param imagen Imagen a almacenar en disco
	 * @param rutaFichero Ruta de la imagen donde vamos a salvar la imagen
	 * @param formato Formato de la imagen al almacenarla en disco
	 * @return Booleano indicando si se consiguio salvar con exito la imagen
	 */
	public String salvarImagen(final BufferedImage imagen, 
			final String rutaFichero, final String nombre, final String formato, boolean reemplazar) throws Exception {
 
		String strFileName = nombre;
		try {
			// Comprobacion de parametros
			if (imagen != null && rutaFichero != null && formato != null) {
				File dest = new File(rutaFichero, nombre);
				if (dest.exists()) {
					if (reemplazar) {
						if (!dest.delete()) {
							logger.error("No se ha eliminado el fichero {}", dest.getAbsolutePath());
						}
					} else {
						//Renombrar
						strFileName = ProcesadorImagenes.rename(nombre, rutaFichero);
					}
				}
				ImageIO.write(imagen, formato, new File( rutaFichero + "/" + strFileName ));
				return strFileName;
				
			} else {
				// Fallo en los parametros 
				return "";
			}
		} catch (Exception e) {
			
			return "";
		}
	}
	public static String rename(String name, String dir) throws Exception {
		String tmp = "";
		String type	=	name.substring(name.lastIndexOf('.') + 1, name.length());
		String nom	=	name.substring(0, name.lastIndexOf('.'));
		int i = nom.length() - 1;
		int j = 0;
		try {
			while (i >= 0) {
				j = Integer.parseInt(nom.substring(i, nom.length()));
				i--;
			}
		} catch (NumberFormatException e) {
			i++;
			if (i < nom.length()) {
				j = Integer.parseInt(nom.substring(i, nom.length())) + 1;	
			} else {
				j = 1;
			}
		}
		if (i == -1) {
			tmp = nom + "-" + j + "." + type;
		} else {
			tmp = nom.substring(0, i) + j + "." + type;
		}
		File dest = new File(dir + tmp);
		if (dest.exists()) {
			return rename(tmp, dir);
		}
		return tmp;

	}
 
	
}