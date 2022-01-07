import de.cerus.jgif.GifImage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class main {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean isArgsValid(String inputPath, String nbLoop) {
        boolean isValid = true;

        if(StringUtils.isAllBlank(inputPath, nbLoop)) {
            isValid = false;
        }

        if (!isValidFilePath(inputPath)) {
            isValid = false;
        }

        try {
            int t = Integer.parseInt(nbLoop);

            // - 1 loop min
            if(t < 1) {
                isValid = false;
            }
        } catch (NumberFormatException e) {
            isValid = false;
        }

        return isValid;
    }

    private static GifImage loadGifFile(String filePath) {
        File f = new File(filePath);

        GifImage gif = new GifImage();

        try {
            gif.loadFrom(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return gif;
    }

    private static File makeOutputFile(String pathWithName) {
        String outputFilePath = FilenameUtils.getFullPath(pathWithName);
        String outputFileName = String.format("%s-output.gif", FilenameUtils.getBaseName(pathWithName));

        return new File(
                FilenameUtils.concat(outputFilePath, outputFileName)
        );
    }

    public static void main(String[] args) {
        String inputPath = args[0];
        String nbLoop = args[1];

        if(!isArgsValid(inputPath, nbLoop)) {
            return;
        }

        int nbLoopParsed = Integer.parseInt(nbLoop);

        GifImage inputGif = loadGifFile(inputPath);
        GifImage outputGif = loadGifFile(inputPath);

        List<BufferedImage> frames = inputGif.getFrames();

        IntStream.range(1, nbLoopParsed)
                .forEach(e -> {
                    outputGif.getFrames().addAll(frames);
                });

        File outputFile = makeOutputFile(inputPath);

        outputGif.setOutputFile(outputFile);
        outputGif.save();
    }
}
