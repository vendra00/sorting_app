package com.sorting.sorting;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Stack;

/**
 * JavaFX App
 * Sorting Algorithm Visualizer
 * @Author: Gabriel Vendramini
 * @Date: 2023-12-11
 */
public class SortingVisualizerApp extends Application {

    private Canvas canvas;
    private Label timerLabel;
    private long startTime;
    private int[] array;
    private int i, j;
    private boolean isSorting = false;

    /**
     * This method is called when the JavaFX application is started
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        // Initialize the canvas before using it
        canvas = new Canvas(300, 250);
        canvas.widthProperty().bind(primaryStage.widthProperty());

        // Label to display the time
        timerLabel = new Label("Time: 0 ms");

        // Now initialize the array, as canvas is already initialized
        initializeArray();

        // Description label
        Label descriptionLabel = new Label();
        descriptionLabel.setWrapText(true); // Enable text wrapping
        descriptionLabel.setMaxWidth(280); // Set max width for wrapping

        // ComboBox for sorting algorithm selection
        ComboBox<String> sortingAlgorithms = new ComboBox<>();
        sortingAlgorithms.getItems().addAll("Bubble Sort", "Quick Sort", "Insertion Sort", "Heap Sort", "Radix Sort");
        sortingAlgorithms.setValue("Bubble Sort");

        // Set initial description
        descriptionLabel.setText(getSortingDescription("Bubble Sort"));

        sortingAlgorithms.valueProperty().addListener((observable, oldValue, newValue) -> {
            descriptionLabel.setText(getSortingDescription(newValue));
        });

        Button drawButton = new Button("Draw");
        Button sortButton = new Button("Sort");

        drawButton.setOnAction(event -> {
            initializeArray(); // Initialize the array with random values
            drawArray(canvas.getGraphicsContext2D(), -1); // Draw the initial unsorted array
        });

        sortButton.setOnAction(event -> {
            if (!isSorting) {
                isSorting = true;
                startTime = System.nanoTime(); // Start timing
                String selectedAlgorithm = sortingAlgorithms.getValue();
                GraphicsContext gc = canvas.getGraphicsContext2D();

                if ("Bubble Sort".equals(selectedAlgorithm)) {
                    bubbleSortVisualization(gc);
                    sortButton.setDisable(true);
                } else if ("Quick Sort".equals(selectedAlgorithm)) {
                    quickSortVisualization(gc); // Pass the GraphicsContext to the method
                    sortButton.setDisable(true);
                } else if ("Insertion Sort".equals(selectedAlgorithm)) {
                    insertionSortVisualization(gc);
                    resetSortingVariables(); // Reset the sorting variables
                    sortButton.setDisable(true);
                } else if ("Heap Sort".equals(selectedAlgorithm)) {
                    heapSortVisualization(gc);
                    resetSortingVariables(); // Reset the sorting variables
                    sortButton.setDisable(true);
                } else if ("Radix Sort".equals(selectedAlgorithm)) {
                    //TODO Implement Radix Sort
                }
            }
        });

        // Disable "Sort" button initially if you want to force users to "Draw" first
        sortButton.setDisable(true);

        // Enable "Sort" button when "Draw" is clicked
        drawButton.setOnAction(event -> {
            initializeArray(); // Initialize the array with random values
            drawArray(canvas.getGraphicsContext2D(), -1); // Draw the initial unsorted array
            sortButton.setDisable(false); // Enable the "Sort" button after drawing the array
        });

        // Combine description and buttons in a single VBox
        VBox combinedContainer = new VBox(10);
        combinedContainer.setAlignment(Pos.CENTER);
        combinedContainer.getChildren().addAll(descriptionLabel, sortingAlgorithms, drawButton, sortButton);

        // Layout using BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(timerLabel);
        borderPane.setCenter(canvas);
        borderPane.setBottom(combinedContainer);

        // Scene and Stage setup
        Scene scene = new Scene(borderPane, 850, 500); // Adjust size as needed
        primaryStage.setTitle("Sorting Algorithm Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Get the description for the selected sorting algorithm
     * @param sortType The selected sorting algorithm
     * @return The description of the sorting algorithm
     */
    private String getSortingDescription(@NotNull String sortType) {
        return switch (sortType) {
            case "Bubble Sort" ->
                    "Bubble Sort is a simple sorting algorithm that repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.";
            case "Quick Sort" ->
                    "Quick Sort is a highly efficient sorting algorithm and is based on partitioning of array of data into smaller arrays.";
            case "Insertion Sort" ->
                    "Insertion Sort is a simple sorting algorithm that builds the final sorted array one item at a time.";
            case "Heap Sort" ->
                    "Heap Sort is a comparison-based sorting technique based on Binary Heap data structure.";
            case "Radix Sort" ->
                    "Radix Sort is a non-comparative sorting algorithm. It avoids comparison by creating and distributing elements into buckets according to their radix.";
            default -> "";
        };
    }

    /**
     * Initialize the array with random values
     */
    private void initializeArray() {
        array = new int[50]; // example size
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * canvas.getHeight());
        }
        i = 0;
        j = 0;
    }

    /**
     * Bubble Sort Visualization
     * @param gc GraphicsContext object to draw on the canvas
     */
    private void bubbleSortVisualization(GraphicsContext gc) {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Continuously update the timer label as the sort progresses
                Platform.runLater(() -> updateTimer(System.nanoTime()));

                if (isSorting) {
                    if (i < array.length - 1) {
                        if (j < array.length - i - 1) {
                            if (array[j] > array[j + 1]) {
                                // Swap elements
                                int temp = array[j];
                                array[j] = array[j + 1];
                                array[j + 1] = temp;
                            }
                            j++;
                            // Update the canvas after each swap
                            Platform.runLater(() -> drawArray(gc, j));
                        } else {
                            j = 0;
                            i++;
                        }
                    } else {
                        // Sorting is done, stop the animation
                        isSorting = false;
                        this.stop();
                        // Update the timer one last time with the final duration
                        Platform.runLater(() -> updateTimer(System.nanoTime()));
                    }
                }
            }
        };
        startTime = System.nanoTime(); // Start the timer when the sort starts
        animationTimer.start();
    }

    /**
     * Quick Sort Visualization
     * @param gc GraphicsContext object to draw on the canvas
     */
    private void quickSortVisualization(GraphicsContext gc) {
        AnimationTimer timer = new AnimationTimer() {
            private final Stack<int[]> stack = new Stack<>(); // Stack to replace the recursive calls

            @Override
            public void handle(long now) {
                Platform.runLater(() -> updateTimer(System.nanoTime()));

                if (!stack.isEmpty() && isSorting) {
                    int[] range = stack.pop();
                    int begin = range[0];
                    int end = range[1];

                    if (begin < end) {
                        int partitionIndex = partition(array, begin, end, gc);

                        // Add sub-ranges to the stack
                        if (partitionIndex - 1 > begin) {
                            stack.push(new int[]{begin, partitionIndex - 1});
                        }
                        if (partitionIndex + 1 < end) {
                            stack.push(new int[]{partitionIndex + 1, end});
                        }
                    }
                } else if (stack.isEmpty()) {
                    isSorting = false;
                    this.stop();
                    Platform.runLater(() -> updateTimer(System.nanoTime()));
                }
            }

            @Override
            public void start() {
                super.start();
                stack.push(new int[]{0, array.length - 1});
                isSorting = true;
            }
        };

        startTime = System.nanoTime();
        timer.start();
    }

    /**
     * Insertion Sort Visualization
     * @param gc GraphicsContext object to draw on the canvas
     */
    private void insertionSortVisualization(GraphicsContext gc) {
        AnimationTimer timer = new AnimationTimer() {
            final int n = array.length;
            int i = 1;
            int j = i;
            int key = array[i];

            @Override
            public void handle(long now) {
                if (i < n) {
                    if (j >= 0 && array[j] > key) {
                        array[j + 1] = array[j];
                        j--;

                        // Update the canvas after each shift
                        Platform.runLater(() -> drawArray(gc, j));
                    } else {
                        array[j + 1] = key;

                        // Move to the next element
                        i++;
                        if (i < n) {
                            j = i - 1;
                            key = array[i];
                        }

                        // Update the canvas after inserting the key
                        Platform.runLater(() -> drawArray(gc, j));
                    }
                } else {
                    // Sorting is done, stop the animation
                    this.stop();
                    Platform.runLater(() -> updateTimer(System.nanoTime())); // Update the timer
                }
            }
        };

        startTime = System.nanoTime();
        timer.start();
    }

    /**
     * Heap Sort Visualization
     * @param gc GraphicsContext object to draw on the canvas
     */
    private void heapSortVisualization(GraphicsContext gc) {
        AnimationTimer timer = new AnimationTimer() {
            final int n = array.length;
            int i = n / 2 - 1; // Start index for building the heap
            int j = n - 1; // Start index for extracting elements from the heap
            boolean buildingHeap = true; // Flag to indicate whether we are still building the heap

            @Override
            public void handle(long now) {
                if (buildingHeap) {
                    if (i >= 0) {
                        heapify(array, n, i); // Heapify the subtree rooted at index i
                        i--;
                    } else {
                        buildingHeap = false;
                        i = n / 2 - 1; // Reset i for the next phase
                    }
                } else {
                    if (j >= 0) {
                        // Swap array[0] with array[j]
                        int temp = array[0];
                        array[0] = array[j];
                        array[j] = temp;

                        heapify(array, j, 0); // Heapify the reduced heap
                        j--;
                    } else {
                        this.stop(); // Stop the animation timer as sorting is done
                    }
                }

                // Update the canvas after each step
                Platform.runLater(() -> drawArray(gc, j));
            }
        };

        timer.start();
    }

     /**
     * Partition the array
     * @param array The array to partition
     * @param begin The beginning index
     * @param end The ending index
     * @param gc GraphicsContext object to draw on the canvas
     * @return The index of the pivot
     */
    private int partition(int @NotNull [] array, int begin, int end, GraphicsContext gc) {
        int pivot = array[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (array[j] <= pivot) {
                i++;

                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                // Visualize the swap
                int finalI = i;
                int finalJ = j;
                Platform.runLater(() -> drawSwap(gc, finalI, finalJ));
            }
        }

        int temp = array[i + 1];
        array[i + 1] = array[end];
        array[end] = temp;

        // Visualize the final swap with the pivot
        final int newPivotIndex = i + 1;
        Platform.runLater(() -> drawSwap(gc, newPivotIndex, end));

        return newPivotIndex;
    }

    /**
     * Heapify the subtree rooted at index i
     * @param arr The array to heapify
     * @param n The size of the heap
     * @param i The root index
     */
    private void heapify(int[] arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }

        // If right child is larger than largest so far
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
    }

    /**
     * Draw the swap
     * @param gc GraphicsContext object to draw on the canvas
     */
    private void drawSwap(GraphicsContext gc, int index1, int index2) {
        // Redraw the array with the swapped elements highlighted
        drawArrayWithHighlights(gc, index1, index2);
    }


    /**
     * Update the timer label
     * @param currentTime Current time in nanoseconds
     */
    private void updateTimer(long currentTime) {
        long elapsedTime = (currentTime - startTime) / 1_000_000; // Convert to milliseconds
        long seconds = elapsedTime / 1000; // Convert milliseconds to seconds
        long milliseconds = elapsedTime % 1000; // Get remaining milliseconds
        timerLabel.setText(String.format("Time: %d.%03d s", seconds, milliseconds));
    }

    /**
     * Draw the array on the canvas
     * @param gc GraphicsContext object to draw on the canvas
     * @param highlightedIndex The index of the bar to highlight
     */
    private void drawArray(@NotNull GraphicsContext gc, int highlightedIndex) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double barWidth = canvas.getWidth() / array.length;
        for (int k = 0; k < array.length; k++) {
            if (k == highlightedIndex) {
                gc.setFill(Color.YELLOW); // Highlight the current bar
            } else {
                gc.setFill(Color.BLUE); // Other bars remain blue
            }
            // Draw each bar with a small margin
            gc.fillRect(k * barWidth, canvas.getHeight() - array[k], barWidth - 2, array[k]);
        }
    }

    private void drawArrayWithHighlights(@NotNull GraphicsContext gc, int index1, int index2) {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        double barWidth = canvas.getWidth() / array.length;
        for (int k = 0; k < array.length; k++) {
            if (k == index1 || k == index2) {
                gc.setFill(Color.YELLOW); // Highlight the bars at index1 and index2
            } else {
                gc.setFill(Color.BLUE); // Other bars remain blue
            }
            gc.fillRect(k * barWidth, canvas.getHeight() - array[k], barWidth - 2, array[k]);
        }
    }

    /**
     * Reset the sorting variables
     */
    private void resetSortingVariables() {
        i = 0;
        j = 0;
        isSorting = false; // This might be set to true again in the specific sorting method
    }

    /**
     * Main method
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}


