package be.ac.ucl.lfsab1509.bouboule.game.level;

import java.io.IOException;
import java.util.Iterator;

import be.ac.ucl.lfsab1509.bouboule.game.body.Arena;
import be.ac.ucl.lfsab1509.bouboule.game.body.Bouboule;
import be.ac.ucl.lfsab1509.bouboule.game.gameManager.GraphicManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LevelLoader {

	private XmlReader 			reader;
	private Element 			root;
	private Element 			file;

	public LevelLoader() {

		this.reader = new XmlReader();
		try {
			root = reader.parse( 
					Gdx.files.internal("level/levels.xml")
					);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadLevel(String level) {

		file = root.getChildByName(level);

	}

	public void readLevelBouboule(GraphicManager graphicManager) {


		Array<Element> maps = file.getChildrenByName("Bouboule");

		for (Iterator<Element> map = maps.iterator(); map
				.hasNext();) {
			Element boub = map.next();

			float radius			= Float.parseFloat(boub.getAttribute("radius"));
			BodyType bodyType		= BodyType.DynamicBody;
			float density			= Float.parseFloat(boub.getAttribute("density"));
			float elasticity		= Float.parseFloat(boub.getAttribute("elasticity"));
			float px				= Float.parseFloat(boub.getAttribute("px"));
			float py				= Float.parseFloat(boub.getAttribute("py"));
			float angle				= Float.parseFloat(boub.getAttribute("angle"));
			String texRegionPath 	= boub.getAttribute("texRegionPath");
			String jsonFile 		= boub.getAttribute("jsonFile");
			String jsonName 		= boub.getAttribute("jsonName");
			short entity			= Short.parseShort(boub.getAttribute("entity"));
			int IALevel				= Integer.parseInt(boub.getAttribute("IALevel"));


			graphicManager.addBody( new Bouboule(radius, bodyType, density,
					elasticity, px, py, angle,texRegionPath, 
					jsonFile, jsonName, entity, IALevel));
			

					
			
			Gdx.app.log("XML",

					"radius :"+
							radius			+" bodyType :"+
							bodyType		+" density :"+
							density			+" elasticity:"+
							elasticity		+" px:"+
							px				+" py :"+
							py				+" angle :"+
							angle				+" tex :"+
							texRegionPath 	+" jsonFile :"+
							jsonFile 		+" name :"+
							jsonName 		+" entity :"+
							entity			+" IA :"+
							IALevel
					);


		}
	}
	
	public void readLevelArena(GraphicManager graphicManager) {
		
		Element aren = file.getChildByName("Arena");	
		
		
		float radius			= Float.parseFloat(aren.getAttribute("radius"));
		float px				= Float.parseFloat(aren.getAttribute("px"));
		float py				= Float.parseFloat(aren.getAttribute("py"));
		float angle				= Float.parseFloat(aren.getAttribute("angle"));
		String texRegionPath 	= aren.getAttribute("texRegionPath");
		String jsonFile 		= aren.getAttribute("jsonFile");
		String jsonName 		= aren.getAttribute("jsonName");
		short entity			= Short.parseShort(aren.getAttribute("entity"));
		
		graphicManager.addBody(new Arena( radius, px, py,  angle, texRegionPath, 
				 jsonFile,  jsonName, entity));
		
	}

}
