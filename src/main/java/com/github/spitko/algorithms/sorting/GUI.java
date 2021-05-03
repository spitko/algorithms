package com.github.spitko.algorithms.sorting;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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
	private JButton copyButton2;
	private JButton copyButton1;
	private JLabel parameter1Label;
	private JSpinner parameter2Spinner;
	private JLabel parameter2Label;
	private JSpinner countSpinner;
	private JButton batchButton;
	private JTextArea batchOutputTextArea;
	private JProgressBar batchProgressBar;
	private JButton copyButton3;
	private JTabbedPane tabbedPane;

	private InverseSort inverseSort;
	private int[] sortedArray;

	public GUI() {
		$$$setupUI$$$();
		algorithmBox.addActionListener(e -> {
			Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
			inverseSort = alg.inverseSort;
			updateParameters(alg.parameters);
		});
		algorithmBox.setSelectedIndex(0);
		arrayLengthSpinner.addChangeListener(e -> updateSpinners());
		generateButton.addActionListener(e -> {
			sortedArray = generateSortedArray();
			sortedArrayTextField.setText(Arrays.toString(sortedArray));
			updateSpinners();
		});
		generateButton.doClick();
		shuffleButton.addActionListener(e -> {
			int[] arr = sortedArray.clone();
			shuffleArray(arr);
			shuffledArrayTextField.setText(Arrays.toString(arr));
		});
		shuffleButton.doClick();
		copyButton1.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sortedArrayTextField.getText()), null);
		});
		copyButton2.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(shuffledArrayTextField.getText()), null);
		});
		copyButton3.addActionListener(e -> {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(batchOutputTextArea.getText()), null);
		});
		batchButton.addActionListener(e -> {
			int count = (int) countSpinner.getValue();
			batchProgressBar.setMaximum(count);
			batchProgressBar.setValue(0);
			batchOutputTextArea.setText(null);
			copyButton3.setEnabled(false);
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
					copyButton3.setEnabled(true);
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

	private void shuffleArray(int[] arr) {
		Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
		if (Algorithm.Quickselect == alg) {
			int shuffles = ((InverseQuickselect) inverseSort).shuffle(arr, (int) parameter1Spinner.getValue(), (int) parameter2Spinner.getValue());
			System.out.println(shuffles + " shuffles were done");
		} else if (Algorithm.Quicksort == alg) {
			inverseSort.shuffle(arr); // no parameters
		} else if (Algorithm.HeapSort == alg) {
			((InverseHeapify) inverseSort).randomHeapify(arr);
		} else {
			inverseSort.shuffle(arr, (int) parameter1Spinner.getValue());
		}
	}

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

	void updateParameters(Parameter[] parameters) {
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
			updateSpinners();
		}
	}

	void updateSpinners() {
		Algorithm alg = (Algorithm) algorithmBox.getSelectedItem();
		int arrayLength = (int) arrayLengthSpinner.getValue();
		for (Parameter parameter : alg.parameters) {
			parameter.updateSpinnerModel(arrayLength);
		}
	}

	private void createUIComponents() {
		algorithmBox = new JComboBox<>(Algorithm.values());
		arrayLengthSpinner = new JSpinner(new SpinnerNumberModel(10, 5, 50, 1));
		minValueSpinner = new JSpinner(new SpinnerNumberModel(1, -99, 99, 1));
		maxValueSpinner = new JSpinner(new SpinnerNumberModel(50, -99, 99, 1));
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
		copyButton2 = new JButton();
		copyButton2.setText("Copy");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(copyButton2, gbc);
		copyButton1 = new JButton();
		copyButton1.setText("Copy");
		gbc = new GridBagConstraints();
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		main.add(copyButton1, gbc);
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
		copyButton3 = new JButton();
		copyButton3.setEnabled(false);
		copyButton3.setText("Copy all");
		panel6.add(copyButton3, BorderLayout.EAST);
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
		String label;
		Function<Integer /*arr.length*/, Integer> maxValueFunction;
		SpinnerNumberModel model;

		public Parameter(String label, int minValue, Function<Integer /*arr.length*/, Integer> maxValueFunction) {
			this.label = label;
			this.maxValueFunction = maxValueFunction;
			this.model = new SpinnerNumberModel(minValue + 1, minValue, 2, 1);
		}

		void updateSpinnerModel(int arrayLength) {
			int newMax = maxValueFunction.apply(arrayLength);
			this.model.setMaximum(newMax);
			if ((int) this.model.getValue() > newMax) {
				this.model.setValue(newMax);
			}
		}
	}
}
