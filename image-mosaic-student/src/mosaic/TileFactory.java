/*
 * Copyright 2017 Marc Liberatore.
 */

package mosaic;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import images.ImageUtils;

public class TileFactory
{
	public final int tileWidth;
	public final int tileHeight;
	// TODO: you will NOT be keeping this array in your final code;
	// see assignment description for details
	// private final int[] hues;
	Map<Integer, List<BufferedImage>> huesMap = null;

	/**
	 * 
	 * @param colors     the palette of RGB colors for this TileFactory
	 * @param tileWidth  width (in pixels) for each tile
	 * @param tileHeight height (in pixels) for each tile
	 */
	public TileFactory(int[] colors, int tileWidth, int tileHeight)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;

		// TODO: when you replace the int[] hues, be sure to replace this code, as well
		// hues = new int[colors.length];
		huesMap = new HashMap<Integer, List<BufferedImage>>();

		for (int i = 0; i < colors.length; i++)
		{
			int hueKey = Math.round(ImageUtils.hue(colors[i]));
			List<BufferedImage> j = new ArrayList<BufferedImage>();
			huesMap.put(hueKey, j);
		}
	}

	/**
	 * Returns the distance between two hues on the circle [0,256).
	 * 
	 * @param hue1
	 * @param hue2
	 * @return the distance between two hues.
	 */
	static int hueDistance(int hue1, int hue2)
	{
		int diff = Math.abs(hue2 - hue1);
		if (diff < 256 / 2)
		{
			return diff;
		} else
		{
			return 256 - diff;
		}
	}

	/**
	 * Returns the closest hue from the fixed palette this TileFactory contains.
	 * 
	 * @param hue
	 * @return the closest hue from the palette
	 */
	Integer closestHue(int hue)
	{
		int minKey = 0;
		int min = 500;
		for (int h : huesMap.keySet())
		{
			int dist = hueDistance(hue, h);
			// System.out.println("hueDistance: " + hue + ", " + h + " = " + dist);
			if (dist < min)
			{
				min = dist;
				minKey = h;
			}
		}
		return minKey;
	}

	/**
	 * Adds an image to this TileFactory for later use.
	 * 
	 * @param image the image to add
	 */
	public void addImage(BufferedImage image)
	{
		image = ImageUtils.resize(image, tileWidth, tileHeight);

		int avgHue = ImageUtils.averageHue(image);

		int closestHueInColorKey = closestHue(avgHue);

		List<BufferedImage> imageList = huesMap.get(closestHueInColorKey);
		// System.out.println("addImage1: " + avgHue + ", " + "closestHueInColorKey: " +
		// ", " + imageList);

		if (imageList == null)
			imageList = new ArrayList<BufferedImage>();
		imageList.add(image);
		// System.out.println("addImage2: " + avgHue + ", " + imageList.size());

		huesMap.put(closestHueInColorKey, imageList);
		// System.out.println("addImage3: huesMap size= " + huesMap.size());

		// TODO: add the image to the appropriate place in your map from hues to lists
		// of images
	}

	/**
	 * Returns the next tile from the list associated with the hue most closely
	 * matching the input hue.
	 * 
	 * The returned values should cycle through the list. Each time this method is
	 * called, the next tile in the list will be returned; when the end of the list
	 * is reached, the cycle starts over.
	 * 
	 * @param hue the color to match
	 * @return a tile matching hue
	 */
	public BufferedImage getTile(int hue)
	{
		// TODO: return an appropriate image from your map of hues to lists of images;
		// see assignment description for details
		int closestKey = closestHue(hue);

		List<BufferedImage> list = huesMap.get(closestKey);
		// System.out.println("getTile: " + hue + ", " + closestKey + ", " +
		// list.size());
		// get first element of current list as n
		// rotate list; replace current list w new list
		// return element n
		BufferedImage firstImage = list.get(0);
		Collections.rotate(list, list.size() - 1);
		return firstImage;
	}

	public void printMap()
	{
		System.out.println("map size= " + huesMap.size());
		for (Integer i : huesMap.keySet())
		{
			System.out.println("key: " + i + " list size: " + huesMap.get(i).size());
		}

	}
}
