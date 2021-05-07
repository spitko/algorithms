package com.github.spitko.algorithms.sorting;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Layout for GUI is auto-generated using IntelliJ GUI Designer
 */
@SuppressWarnings("ConstantConditions")
public class GUI {

	private static final String LOOK_AND_FEEL = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	private JComboBox<Algorithm> algorithmBox;
	private JSpinner arrayLengthSpinner;
	private JSpinner minValueSpinner;
	private JSpinner maxValueSpinner;
	private JTextField sortedArrayTextField;
	private JButton generateButton;
	private JSpinner parameter1Spinner;
	private JButton shuffleButton;
	private JTextField shuffledArrayTextField;
	private JPanel main;
	private JButton shuffledArrayCopyButton;
	private JButton sortedArrayCopyButton;
	private JLabel parameter1Label;
	private JSpinner parameter2Spinner;
	private JLabel parameter2Label;
	private JSpinner countSpinner;
	private JButton batchButton;
	private JTextArea batchOutputTextArea;
	private JProgressBar batchProgressBar;
	private JButton batchOutputCopyButton;
	private JTabbedPane tabbedPane;

	// Currently selected algorithm
	private InverseSort inverseSort;
	// Updated on "Generate" button click
	private int[] sortedArray;

	public GUI() {
		$$$setupUI$$$();

		// New algorithm is selected
		algorithmBox.addActionListener(e -> {
			Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
			inverseSort = alg.inverseSort;
			updateParameters(alg.parameters);
		});

		// Pre-select first algorithm
		algorithmBox.setSelectedIndex(0);

		// Update parameter spinners on array length change
		arrayLengthSpinner.addChangeListener(e -> updateSpinners());

		// Generate button is clicked
		generateButton.addActionListener(e -> {
			sortedArray = generateSortedArray();
			sortedArrayTextField.setText(Arrays.toString(sortedArray));
			updateSpinners();
		});

		//  click to populate sorted array text field on startup
		generateButton.doClick();

		// Shuffle button is clicked
		shuffleButton.addActionListener(e -> {
			int[] arr = sortedArray.clone();
			shuffleArray(arr);
			shuffledArrayTextField.setText(Arrays.toString(arr));
		});

		// click to populate shuffled array text field on startup
		shuffleButton.doClick();

		// Copy buttons
		sortedArrayCopyButton.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sortedArrayTextField.getText()), null);
		});
		shuffledArrayCopyButton.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shuffledArrayTextField.getText()), null);
		});
		batchOutputCopyButton.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(batchOutputTextArea.getText()), null);
		});

		// Generate is clicked in batch tab
		batchButton.addActionListener(e -> {
			// array count set in batch tab
			int count = (int) countSpinner.getValue();

			// Reset progress bar
			batchProgressBar.setMaximum(count);
			batchProgressBar.setValue(0);

			// Clear output text area
			batchOutputTextArea.setText(null);

			// Disable copy button while generating is in progress
			batchOutputCopyButton.setEnabled(false);

			// generate arrays in background thread, so swing does not hang
			SwingWorker<Void, String> worker = new SwingWorker<>() {
				@Override
				protected Void doInBackground() {
					for (int i = 1; i <= count; i++) {
						int[] arr = generateSortedArray();
						shuffleArray(arr);
						publish(Arrays.toString(arr) + '\n');
					}
					return null;
				}

				@Override
				protected void process(List<String> chunks) {
					batchOutputTextArea.append(String.join("", chunks));
					batchProgressBar.setValue(batchProgressBar.getValue() + chunks.size());
				}

				@Override
				protected void done() {
					batchOutputCopyButton.setEnabled(true);
				}
			};
			worker.execute();
		});
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(LOOK_AND_FEEL);
		} catch (Exception ignored) {
			// fall back to default
		}
		SwingUtilities.invokeLater(GUI::createAndShowGUI);
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Algorithm input generator");
		frame.setContentPane(new GUI().tabbedPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}

	/**
	 * Shuffle selected array in-place, using selected algorithm
	 * @param arr Array to shuffle
	 */
	private void shuffleArray(int[] arr) {
		Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
		switch (alg) {
			case Quickselect: // 2 parameters
				int shuffles = ((InverseQuickselect) inverseSort).shuffle(arr, (int) parameter1Spinner.getValue(), (int) parameter2Spinner.getValue());
				System.out.println(shuffles + " shuffles were done");
				break;
			case Quicksort: // no parameters
				inverseSort.shuffle(arr);
				break;
			case HeapSort: // no parameters, different method
				((InverseHeapify) inverseSort).randomHeapify(arr);
				break;
			default: // only 1 parameter
				inverseSort.shuffle(arr, (int) parameter1Spinner.getValue());
				break;
		}
	}

	/**
	 * Generate and return a new sorted array, using parameters set in GUI
	 * @return Sorted array
	 */
	private int[] generateSortedArray() {
		int arrayLength = (int) arrayLengthSpinner.getValue();
		int minValue = (int) minValueSpinner.getValue();
		int maxValue = (int) maxValueSpinner.getValue() + 1;
		if (arrayLength > maxValue - minValue) {
			JOptionPane.showMessageDialog(tabbedPane, "Array length must be greater than (max - min)", "Error", JOptionPane.ERROR_MESSAGE);
			throw new IllegalArgumentException();
		}
		return inverseSort.generateSortedArray(minValue, maxValue, arrayLength);
	}

	/**
	 * Called when new algorithm is selected, change parameter spinners and labels (based on algorithm)
	 * @param parameters Selected algorithm's parameters
	 */
	private void updateParameters(Parameter[] parameters) {
		parameter1Label.setVisible(false);
		parameter1Spinner.setVisible(false);
		parameter2Label.setVisible(false);
		parameter2Spinner.setVisible(false);
		if (parameters.length > 0) {
			parameter1Label.setText(parameters[0].label);
			parameter1Label.setVisible(true);
			parameter1Spinner.setModel(parameters[0].model);
			parameter1Spinner.setVisible(true);
			if (parameters.length > 1) {
				parameter2Label.setText(parameters[1].label);
				parameter2Label.setVisible(true);
				parameter2Spinner.setModel(parameters[1].model);
				parameter2Spinner.setVisible(true);
			}
			// Ensure spinners have correct max value
			updateSpinners();
		}
	}

	/**
	 * Called whenever array length or algorithm is changed
	 */
	private void updateSpinners() {
		Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
		int arrayLength = (int) arrayLengthSpinner.getValue();
		for (Parameter parameter : alg.parameters) {
			parameter.updateSpinnerModel(arrayLength);
		}
	}

	private void createUIComponents() {
		// Algorithm selection dropdown
		algorithmBox = new JComboBox<>(Algorithm.values());

		// First row spinners
		arrayLengthSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 50, 1));
		minValueSpinner = new JSpinner(new SpinnerNumberModel(1, -99, 99, 1));
		maxValueSpinner = new JSpinner(new SpinnerNumberModel(50, -99, 99, 1));

		// Batch mode count spinner
		countSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 1000000, 1));
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		tabbedPane = new JTabbedPane();
		main = new JPanel();
		main.setLayout(new GridBagLayout());
		tabbedPane.addTab("Single", main);
		main.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		GridBagConstraints gbc;
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		main.add(panel1, gbc);
		final JLabel label1 = new JLabel();
		label1.setText("Array length:");
		panel1.add(label1);
		panel1.add(arrayLengthSpinner);
		final JLabel label2 = new JLabel();
		label2.setText("Min value:");
		panel1.add(label2);
		panel1.add(minValueSpinner);
		final JLabel label3 = new JLabel();
		label3.setText("Max value:");
		panel1.add(label3);
		panel1.add(maxValueSpinner);
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		main.add(panel2, gbc);
		final JLabel label4 = new JLabel();
		label4.setText("Algorithm:");
		panel2.add(label4);
		panel2.add(algorithmBox);
		parameter1Label = new JLabel();
		parameter1Label.setText("Parameter 1");
		parameter1Label.setVisible(true);
		panel2.add(parameter1Label);
		parameter1Spinner = new JSpinner();
		parameter1Spinner.setVisible(true);
		panel2.add(parameter1Spinner);
		parameter2Label = new JLabel();
		parameter2Label.setText("Parameter 2");
		parameter2Label.setVisible(false);
		panel2.add(parameter2Label);
		parameter2Spinner = new JSpinner();
		parameter2Spinner.setVisible(false);
		panel2.add(parameter2Spinner);
		shuffledArrayTextField = new JTextField();
		shuffledArrayTextField.setEditable(false);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 0, 5);
		main.add(shuffledArrayTextField, gbc);
		sortedArrayTextField = new JTextField();
		sortedArrayTextField.setEditable(false);
		sortedArrayTextField.setText("");
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 0, 5);
		main.add(sortedArrayTextField, gbc);
		final JLabel label5 = new JLabel();
		label5.setText("Sorted array:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 4, 0, 0);
		main.add(label5, gbc);
		final JLabel label6 = new JLabel();
		label6.setText("Shuffled array:");
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 4, 0, 0);
		main.add(label6, gbc);
		shuffleButton = new JButton();
		shuffleButton.setText("Shuffle");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(shuffleButton, gbc);
		generateButton = new JButton();
		generateButton.setText("Generate");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(generateButton, gbc);
		shuffledArrayCopyButton = new JButton();
		shuffledArrayCopyButton.setText("Copy");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(shuffledArrayCopyButton, gbc);
		sortedArrayCopyButton = new JButton();
		sortedArrayCopyButton.setText("Copy");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(sortedArrayCopyButton, gbc);
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Batch", panel3);
		panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		final JScrollPane scrollPane1 = new JScrollPane();
		panel3.add(scrollPane1, BorderLayout.CENTER);
		batchOutputTextArea = new JTextArea();
		batchOutputTextArea.setEditable(false);
		scrollPane1.setViewportView(batchOutputTextArea);
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout(0, 0));
		panel3.add(panel4, BorderLayout.NORTH);
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panel4.add(panel5, BorderLayout.WEST);
		final JLabel label7 = new JLabel();
		label7.setText("Generate");
		panel5.add(label7);
		panel5.add(countSpinner);
		final JLabel label8 = new JLabel();
		label8.setText("random arrays");
		panel5.add(label8);
		batchButton = new JButton();
		batchButton.setText("Generate");
		panel4.add(batchButton, BorderLayout.EAST);
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new BorderLayout(0, 0));
		panel3.add(panel6, BorderLayout.SOUTH);
		batchProgressBar = new JProgressBar();
		panel6.add(batchProgressBar, BorderLayout.CENTER);
		batchOutputCopyButton = new JButton();
		batchOutputCopyButton.setEnabled(false);
		batchOutputCopyButton.setText("Copy all");
		panel6.add(batchOutputCopyButton, BorderLayout.EAST);
		label1.setLabelFor(arrayLengthSpinner);
		label2.setLabelFor(minValueSpinner);
		label3.setLabelFor(maxValueSpinner);
		label4.setLabelFor(algorithmBox);
		parameter1Label.setLabelFor(parameter1Spinner);
		parameter2Label.setLabelFor(parameter2Spinner);
		label5.setLabelFor(sortedArrayTextField);
		label6.setLabelFor(shuffledArrayTextField);
		label7.setLabelFor(countSpinner);
		label8.setLabelFor(countSpinner);
	}

	/**
	 * @noinspection ALL
	 */
	public JComponent $$$getRootComponent$$$() {
		return tabbedPane;
	}

	private enum Algorithm {
		BubbleSort(new InverseBubbleSort(), new Parameter("iterations:", 1, n -> n)),
		Heapify(new InverseHeapify(), new Parameter("sinking operations:", 0, n -> n / 2)),
		HeapSort(new InverseHeapify()), // shuffleHeapify() called
		InsertionSort(new InverseInsertionSort(), new Parameter("insertions:", 0, n -> n - 1)),
		Quickselect(new InverseQuickselect(), new Parameter("k:", 0, n -> n - 2), new Parameter("partitions:", 1, n -> n - 1)),
		SelectionSort(new InverseSelectionSort(), new Parameter("swaps:", 0, n -> n - 1)),
		Quicksort(new InverseBubbleSort()), // shuffle() with no parameters is called
		;

		private final InverseSort inverseSort;
		private final Parameter[] parameters;

		Algorithm(InverseSort inverseSort, Parameter... parameters) {
			this.inverseSort = inverseSort;
			this.parameters = parameters;
		}
	}

	private static class Parameter {
		String label; // Label to be shown in GUI for parameter
		Function<Integer /*arr.length*/, Integer> maxValueFunction; // Function to get max parameter value from array length
		SpinnerNumberModel model; // Parameter's spinner model

		public Parameter(String label, int minValue, Function<Integer /*arr.length*/, Integer> maxValueFunction) {
			this.label = label;
			this.maxValueFunction = maxValueFunction;
			// Maximum value is later updated through updateSpinnerModel
			this.model = new SpinnerNumberModel(minValue + 1, minValue, 2, 1);
		}

		/**
		 * Called on array length change (or algorithm change), in order to update spinner's max value
		 * @param arrayLength new array length
		 */
		void updateSpinnerModel(int arrayLength) {
			int newMax = maxValueFunction.apply(arrayLength);
			this.model.setMaximum(newMax);
			if ((int) this.model.getValue() > newMax) {
				this.model.setValue(newMax);
			}
		}
	}
}
