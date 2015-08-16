package domination.yahoo;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import domination.common.Player;
import domination.common.PlayerPosition;

public class YahooPlayerParser {
	
	public YahooPlayerParser() {
		
	}
	
	public Collection<Player> parse(final String xmlString) throws IOException {
		List<Player> players = new ArrayList<>();
		
		try {
			Document doc = loadXMLFromString(xmlString);
			doc.getDocumentElement().normalize();
	
			NodeList nodes = doc.getElementsByTagName("player");
			
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
		
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					players.add(parsePlayer(element));
				}
			}
		} catch (Exception ex) {
			System.out.println(ex);
			throw new IOException("Failed to parse XML string", ex);
		}

		return players;
	}

	private Player parsePlayer(final Element player) {
		Node nameNode = player.getElementsByTagName("name").item(0);
		String name = getValue("full", (Element) nameNode);
		
		Node positionNode = player.getElementsByTagName("eligible_positions").item(0);
		PlayerPosition position = Enum.valueOf(PlayerPosition.class, getValue("position", (Element) positionNode));
		
		if (name.equals("Denard Robinson")) {
			position = PlayerPosition.RB;
		}
		
		Element playerStats = (Element) player.getElementsByTagName("player_stats").item(0);
		Element stats = (Element) playerStats.getElementsByTagName("stats").item(0);
		double score = computeScore(stats.getElementsByTagName("stat"));
		
		return new Player(YahooKeyGenerator.generateKey(name, position), name, position, 0.0, score);
	}
	
	private double computeScore(final NodeList stats) {
		double score = 0.0;
		
		for (int i = 0; i < stats.getLength(); i++) {
			Node node = stats.item(i);
	
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				score += parseStat(element);
			}
		}
			
		return score;
	}

	private double parseStat(Element element) {
		Integer id = Integer.parseInt(getValue("stat_id", element));
		Double value = Double.parseDouble(getValue("value", element));
		return determineStatValue(id, value);
	}

	private double determineStatValue(Integer id, Double value) {
		double score = 0.0;
		switch (id) {
		case 4: {
			// Passing Yards
			score = value * .04;
			break;
		}
		case 5: {
			// Passing TD
			score = value * 4;
			break;
		}
		case 6: {
			// Interception
			score = value * -1;
			break;
		}
		case 9: {
			// Rushing Yard
			score = value * .1;
			break;
		}
		case 10: {
			// Rushing TD
			score = value * 6;
			break;
		}
		case 11: {
			// Receptions
			score = value * .5;
			break;
		}
		case 12: {
			// Receiving Yards
			score = value * .1;
			break;
		}
		case 13: {
			// Receiving TDs
			score = value * 6;
			break;
		}
		case 15: {
			// Return TDs
			score = value * 6;
			break;
		}
		case 16: {
			// 2-pt Conversion
			score = value * 2;
			break;
		}
		case 18: {
			// Fumbles Lost
			score = value * -2;
			break;
		}
		case 19:
		case 20:
		case 21: {
			// Field goals 0 - 39 Yards
			score = value * 3;
			break;
		}
		case 22: {
			// Field goals 40 - 49 Yards
			score = value * 4;
			break;
		}
		case 23: {
			// Field goals 50+
			score = value * 5;
			break;
		}
		case 29: {
			// PAT made
			score = value * 1;
			break;
		}
		case 31: {
			// Points against
			if (value == 0) {
				score = 10;
			}
			else if (value < 7) {
				score = 7;
			}
			else if (value < 14) {
				score = 4;
			}
			else if (value < 21) {
				score = 1;
			}
			else if (value < 35) {
				score = -1;
			}
			else {
				score = -4;
			}
			
			break;
		}
		case 32: {
			// Sack
			score = value * 1;
			break;
		}
		case 33: {
			// Interception
			score = value * 2;
			break;
		}
		case 34: {
			// Fumble
			score = value *2;
			break;
		}
		case 35: {
			// DEF Touchdown
			score = value * 6;
			break;
		}
		case 36: {
			// Safety
			score = value * 2;
			break;
		}
		case 37: {
			// Blocked Kick
			score = value * 2;
			break;
		}
		case 49: {
			// Kickoff & Punt Return TDs
			score = value * 6;
			break;
		}
		}
		return score;
	}

	private static String getValue(String tag, Element element) {
		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node node = (Node) nodes.item(0);
		return node.getNodeValue();
	}
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
}
