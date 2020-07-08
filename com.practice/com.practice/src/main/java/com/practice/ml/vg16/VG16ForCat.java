package com.practice.ml.vg16;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.slf4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class VG16ForCat {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TrainImageNetVG16.class);
    private static final String TRAINED_PATH_MODEL = TrainImageNetVG16.DATA_PATH + "/model.zip";
    private static ComputationGraph computationGraph;


    public PetType detectCat(File file, Double threshold) throws IOException {
        if (computationGraph == null) {
            computationGraph = loadModel();
        }

        computationGraph.init();
        log.info(computationGraph.summary());
        NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
        INDArray image = loader.asMatrix(new FileInputStream(file));
        DataNormalization scaler = new VGG16ImagePreProcessor();
        scaler.transform(image);
        INDArray output = computationGraph.outputSingle(false, image);
        if (output.getDouble(0) > threshold) {
            return PetType.CAT;
        } else if (output.getDouble(1) > threshold) {
            return PetType.DOG;
        } else {
            return PetType.NOT_KNOWN;
        }
    }


    public ComputationGraph loadModel() throws IOException {
        computationGraph = ModelSerializer.restoreComputationGraph(new File(TRAINED_PATH_MODEL));
        return computationGraph;
    }

}
