package com.SpriteSheeter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Canvas {
    private final Map<String, int[][]> ID_ARRAY_MAP = new LinkedHashMap<>();
    private final Map<String, BufferedImage> LAYERS = new LinkedHashMap<>();
    private final List<String> HIDDEN_LAYERS = new ArrayList<>();
    private BufferedImage POINTER_LAYER;
    private int arraySize;
    private int canvasSize = 0;
    private int spriteSide = 0;


    /**
     * Builds a new BufferedImage out of the unhidden layers.
     *
     * @return a {@code BufferedImage}
     */
    public BufferedImage getCanvas() {
        //Mixing of all bufferedImages in layers in one single image.
        BufferedImage b = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = b.createGraphics();
        int i = 0;
        for (Map.Entry<String, BufferedImage> entry : LAYERS.entrySet()) {
            if (!HIDDEN_LAYERS.contains(entry.getKey())) {
                graphics2D.drawImage(LAYERS.get(entry.getKey()), null, 0, 0);
            }
        }
        return b;
    }

    /**
     * Returns a string with all the data related to the current canvas. This data includes:
     * <ul>
     *    <li>The amount of sprites on each side of the canvas.</li>
     *    <li>The absolute path where the spritesheet is located.</li>
     *    <li>The name of each layer in the canvas and the array of IDs for each layer.</li>
     * </ul>
     *
     * @return {@code String}
     */
    public String getDataString(String picturePath) {
        int x = 0;
        int y = 0;
        StringBuilder variables = new StringBuilder();
        StringBuilder arrayNumbers = new StringBuilder();
        variables.append("//Sprites in side = ").append(arraySize).append("\n");
        variables.append("\n//Sprite side = ").append(spriteSide).append("\n");
        variables.append("\n//Canvas side size = ").append(canvasSize).append("\n");
        variables.append("\n##").append(picturePath).append("##\n\n");
        for (Map.Entry<String, int[][]> array : ID_ARRAY_MAP.entrySet()) {
            variables.append("-\n//Layer: ").append(array.getKey()).append("\nint[][] ")
                    .append(array.getKey()).append(" = {\n{");
            int[][] layerArray = array.getValue();
            for (int i = 0; i < arraySize; i++) {
                for (int j = 0; j < arraySize; j++) {
                    arrayNumbers.append(layerArray[y][x]).append(" ");
                    variables.append(layerArray[y][x]);
                    x++;
                    if (x < arraySize) {
                        variables.append(",");
                    } else {
                        variables.append("}");
                    }
                }
                x = 0;
                y++;
                if (y < arraySize) {
                    variables.append(",\n{");
                } else {
                    variables.append("\n};\n");
                }
            }
            y = 0;
            variables.append("//").append(array.getKey()).append(":").append(arrayNumbers).append("\n-\n");
            arrayNumbers = new StringBuilder();
        }
        variables.append("-");
        return variables.toString();
    }

    /**
     * Returns the BufferedImage resulted by mixing the value returned by {@link #getCanvas()}, with the
     * frame and pointer layers.
     *
     * @return {@code BufferedImage}
     */
    public BufferedImage getFramedCanvas() {
        /*
         * En b es + 2 porque se establece el ancho y el alto del canvas. Es decir, debe tener x initial pixels
         * más 2 porque en cada lado del canvas tiene que haber un pixel extra para el marco.
         * (Lado izquierdo = 1) + canvas = 80 + (lado derecho = 1) > todo suma 82
         * (Parte superior) = 1 + canvas = 80 + (parte inferior = 1) > todo suma 82
         * */

        BufferedImage b = new BufferedImage(canvasSize + 2, canvasSize + 2, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = b.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        graphics2D.drawImage(getCanvas(), 1, 1, canvasSize,
                canvasSize, null);
        graphics2D.drawImage(POINTER_LAYER, 0, 0, canvasSize + 2,
                canvasSize + 2, null);
        graphics2D.dispose();
        return b;
    }

    /**
     * Returns the BufferedImage returned by {@link #getCanvas()}, after applying the specified scale ratio.
     *
     * @param scaleRatio The {@code initialCanvasSize} variable will be multiplied by scaleRatio.
     *
     * @return {@code BufferedImage}
     */
    public BufferedImage getFramelessScaledCanvas(int scaleRatio) {
        int targetSide = (canvasSize * scaleRatio) + 2; //<<<<<<<<<<<< Check this +2
        BufferedImage b = new BufferedImage(targetSide, targetSide, BufferedImage.TYPE_INT_ARGB);
        b.createGraphics().drawImage(getCanvas(), 0, 0, targetSide, targetSide, null);
        return b;
    }

    /**
     * Returns the value to which the specified key is mapped in {@code ID_ARRAY_MAP}, or null if
     * this map contains no mapping for the key,
     *
     * @param idArrayName The key for the desired mapped value.
     *
     * @return {@code ID_ARRAY_MAP.get(idArrayName);}
     * */
    public int[][] getID_ARRAY_MAP(String idArrayName) {
        return ID_ARRAY_MAP.get(idArrayName);
    }

    /**
     * Returns the canvas side size.
     *
     * @return {@code canvasSize}
     * */
    public int getCanvasSize() {
        return canvasSize;
    }

    /**
     * Returns the value to which the specified key is mapped in the {@code LAYERS}, or null if
     * this map contains no mapping for the key,
     *
     * @param layerName The key for the desired mapped value.
     *
     * @return {@code LAYERS.get(layerName);}
     */
    public BufferedImage getLayer(String layerName) {
        return LAYERS.get(layerName);
    }

    /**
     * Returns the map Layers.
     *
     * @return {@code LAYERS}
     * */
    public Map<String, BufferedImage> getLAYERS() {
        return LAYERS;
    }

    public BufferedImage getPOINTER_LAYER() {
        return POINTER_LAYER;
    }

    /**
     * Scales the full canvas, all the layers and the frame layer, using the specified ration.
     *
     * @param scaleRatio
     * @return
     */
    public BufferedImage getScaledCanvas(int scaleRatio) {
        int targetSide = (canvasSize * scaleRatio) + 2;
        BufferedImage b = new BufferedImage(targetSide, targetSide, BufferedImage.TYPE_INT_ARGB);
        b.createGraphics().drawImage(getFramedCanvas(), 0, 0, targetSide, targetSide, null);
        return b;
    }

    public int getSpriteSide() {
        return spriteSide;
    }

    /**
     * Adds a new BufferedImage to the map LAYERS, and a new ID array to the MAP ID_ARRAY_MAP,
     * under the specified String.
     *
     * @param layerName
     */
    public void addNewCanvas(String layerName) {
        BufferedImage canvas = new BufferedImage(canvasSize, canvasSize, BufferedImage.TYPE_INT_ARGB);
        LAYERS.put(layerName, canvas);
        ID_ARRAY_MAP.put(layerName, new int[arraySize][arraySize]);
    }

    /**
     * Builds layers out of imported ID arrays.
     *
     * @param IDArray
     */
    public void buildLayers(Map<String, int[]> IDArray, HashMap<Integer, Sprite> spritesHasmap) {
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC);
        for (Map.Entry<String, int[]> newLayers : IDArray.entrySet()) {
            int sideSprites = (int) Math.sqrt(newLayers.getValue().length);
            int[] currentLayerIds = newLayers.getValue();
            int idCount = 0;
            int xSprite = 0;
            int ySprite = 0;
            int[][] loadedIdArray = new int[sideSprites][sideSprites];
            Graphics2D pictureGraphics = LAYERS.get(newLayers.getKey()).createGraphics();
            pictureGraphics.setComposite(ac);
            for (int y = 0; y < sideSprites; y++) {
                for (int x = 0; x < sideSprites; x++) {
                    pictureGraphics.drawImage(spritesHasmap.get(currentLayerIds[idCount]).getSprite(), xSprite, ySprite, null);
                    loadedIdArray[y][x] = currentLayerIds[idCount];
                    idCount++;
                    xSprite += spriteSide;
                }
                xSprite = 0;
                ySprite += spriteSide;
            }
            pictureGraphics.dispose();
            ID_ARRAY_MAP.put(newLayers.getKey(), loadedIdArray);
        }
    }

    /**
     * Clear the bufferedImage of the specified layer.
     *
     * @param layerToClear
     */
    public void clearLayer(String layerToClear) {
        LAYERS.put(layerToClear, new BufferedImage(canvasSize, canvasSize,
                BufferedImage.TYPE_INT_ARGB));
    }

    /**
     * Clear the bufferedImage of each layer.
     */
    public void clearAllLayers() {
        for (Map.Entry<String, BufferedImage> entry : LAYERS.entrySet()) {
            clearLayer(entry.getKey());
        }
    }

    /**
     * Removes the mapping for a key from LAYERS.
     *
     * @param layerToDelete
     */
    public void deleteLayer(String layerToDelete) {
        LAYERS.remove(layerToDelete);
    }

    /**
     * Removes all the mappings from LAYERS.
     */
    public void deleteAllLayers() {
        LAYERS.clear();
    }

    /**
     * Adds the specified layer to HIDDEN_LAYERS.
     *
     * @param layerName
     */
    public void hideLayer(String layerName) {
        HIDDEN_LAYERS.add(layerName);
    }

    public boolean hasLayer(String layerName) {
        return LAYERS.containsKey(layerName);
    }

    public void initializeCanvas(int side, int newCanvasSize) {
        spriteSide = side;
        canvasSize = newCanvasSize;
        arraySize = canvasSize / spriteSide;

        int frameThickness = 2;
        int pointerSize = canvasSize + frameThickness;
        POINTER_LAYER = new BufferedImage(pointerSize, pointerSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D pictureGraphics = POINTER_LAYER.createGraphics();
        pictureGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
        pictureGraphics.setPaint(Color.BLACK);
        pictureGraphics.drawRect(0, 0, canvasSize + 1, canvasSize + 1);
        pictureGraphics.setColor(Color.RED);
        pictureGraphics.drawRect(1, 1, 15, 15);
        pictureGraphics.dispose();
    }

    /**
     * Removes the specified layer from HIDDEN_LAYERS.
     *
     * @param layerName
     */
    public void showLayer(String layerName) {
        HIDDEN_LAYERS.remove(layerName);
    }
}
