import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * 
 * @author Suraj Sangani This program is used to perform Cross Validation and
 *         Nearest Neighbors. The program calculates the error and standard
 *         deviation of classifying the examples by cross - validation. It also
 *         displays the matrix for examples classified by 1-NN, 2-NN, 3-NN,
 *         4-NN, 5-NN. The input is read using two text files which have a
 *         standard format as specified in the input specifications.
 * 
 */
public class GradedHomework1 {
	private final Path filepath;
	private final static Charset ENCODING = StandardCharsets.UTF_8;
	public static String a, b, c, d, e, f, g;
	public static int q = 0, p = 0;
	public static int x1 = 1;
	public static float iter = 0;
	public static char[][] resultarray;
	public static HashMap<Integer, String> var = new HashMap<Integer, String>();// hashmap
																				// to
																				// store
																				// the
																				// line
																				// in
																				// the
																				// first
																				// file
	public static HashMap<String, Character> vars = new HashMap<String, Character>();// hashmap
																						// to
																						// store
																						// the
																						// example
																						// number
																						// and
																						// sign
	public static HashMap<Integer, String> var1 = new HashMap<Integer, String>();// hashmap
																					// to
																					// store
																					// the
																					// example
																					// number
																					// and
																					// x1
																					// and
																					// x2
	public static HashMap<Integer, String> x1x2lists = new HashMap<Integer, String>();// hashmap
																						// to
																						// store
																						// the
																						// examplenumber
																						// and
																						// the
																						// x1,
																						// x2
																						// and
																						// sign
																						// of
																						// the
																						// example
	public static HashMap<Integer, String> testanswers = new HashMap<Integer, String>();// hashmap
																						// to
																						// store
																						// the
																						// test
																						// answer
																						// and
																						// its
																						// sign

	public GradedHomework1(String filename) {
		filepath = Paths.get(filename);
	}

	public static void main(String args[]) throws IOException {
		GradedHomework1 parser = new GradedHomework1("C:\\test.txt");// enter
																		// path
																		// of
																		// first
																		// text
																		// file
																		// here
		GradedHomework1 parsers = new GradedHomework1("C:\\Test2.txt");// enter
																		// path
																		// of
																		// second
																		// text
																		// file
																		// here
		parser.linebyline();
		parsers.linebylines();
		float errorarray[] = new float[Integer.valueOf(c)];// array to store the
															// error calculated
		int k = 1;
		while (k <= 5) {
			for (int i = 0; i < Integer.valueOf(c); i++) {
				errorarray[i] = test(var.get(i), k);// store the error
													// calculated
			}

			float erroravg = (errorarray[0] + errorarray[1] + errorarray[2]) / 3;// calculate
																					// e
			double variance = (Math.pow((erroravg - errorarray[0]), 2)
					+ Math.pow(erroravg - errorarray[1], 2) + Math.pow(erroravg
					- errorarray[2], 2)) / 2;// calculate variance
			System.out.print("k=" + k);
			System.out.print("e=" + erroravg);
			double standarddeviation = Math.sqrt(variance);// sigma is sqrt of
															// variance
			System.out.println("sigma=" + standarddeviation);
			nearestneighbour(k);// function to print the nearest neighbour
								// matrix
			k++;// 1<=k<=5
		}

	}

	/**
	 * Function to get the store the second file in a tabular format. This
	 * function then calls classification fucntion.
	 *
	 * @param s
	 *            The permutation on which classification is to be done
	 * @param k
	 *            1<=k<=5
	 * @return error in classification
	 */

	public static float test(String s, int k) {
		int examplenumber = 0;
		int examplenumberarray[] = new int[Integer.valueOf(b)];
		s = s.replaceAll("\\s", "");// replace spaces by null
		// System.out.println(s);
		for (int po = 0; po < s.length(); po++) {
			examplenumberarray[po] = (char) s.charAt(po);// store the example
															// number in array
		}
		for (int j = 0; j < Integer.valueOf(d); j++) {
			for (int l = 0; l < Integer.valueOf(e); l++) {
				String b = Integer.toString(j);
				String a = Integer.toString(l);
				String c = a + b;// store the position of .,+,- in the matrix
				if (vars.get(c) != null) {
					if (vars.get(c) == '+' || vars.get(c) == '-') // store only
																	// values
																	// containing
																	// +/-
					{
						// System.out.println(examplenumber);
						// System.out.println(c);
						// store the examplenumber and the x1, x2 and sign of
						// the example in x1x2lists
						x1x2lists
								.put(Character
										.getNumericValue(examplenumberarray[examplenumber]),
										c + Character.toString(vars.get(c)));
						// System.out.println(x1x2lists);
						// x1x2list.add(c+vars.get(c));
						var1.put(examplenumber, c);// store the example number
													// and x1 and x2 in var
						examplenumber++;// iterate examplenumber to appropriate
										// position
					}
				}
			}
		}
		if (k == 1) {
			float error1 = classification(1);
			return error1;
		}
		if (k == 2) {
			float error2 = classification(2);
			return error2;
		}
		if (k == 3) {
			float error3 = classification(3);
			return error3;
		}
		if (k == 4) {
			float error4 = classification(4);
			return error4;
		}
		if (k == 5) {
			float error5 = classification(5);
			return error5;
		}
		return 0;
	}

	public static float classification(int k) {

		int x = (Integer.valueOf(b)) / (Integer.valueOf(a));// x is number of
															// folds
		int count = x;// store the value in count
		int y = 0;
		int z = 0;
		int times = Integer.valueOf(a);// number of times we have to partition
										// the data
		float errors = 0;// variable to store the error calculated
		while (times > 0) {
			HashMap training = new HashMap();// hashmap to store training data
			List testing = new ArrayList<>();// arraylist to store testing data
			HashMap train = new HashMap();// hashmap to store part of training
											// data
			HashMap trainings = new HashMap();// hashmap to store part of
												// training data
			for (int i = y; i < count; i++)// add the elements to the testing
											// set
			{
				testing.add(x1x2lists.get(i));
			}
			// System.out.println("Testing" + testing);
			if (x1x2lists.size() - count <= y)// if difference between number of
												// examples and current count <
												// y
			{
				// System.out.println(x1x2lists);
				for (int iterator = 0; iterator < y; iterator++) {
					// train.add(x1x2lists.get(iterator));
					train.put(iterator, x1x2lists.get(iterator));// add the
																	// examples
																	// to train
					// System.out.println("Train" + train);
				}
				for (int iterator = count; iterator < x1x2lists.size() - 1; iterator++) {
					trainings.put(iterator, x1x2lists.get(iterator));// add the
																		// examples
																		// to
																		// trainings
					// System.out.println("Trainings" + trainings);
				}
				training.putAll(train);// add all examples to training set
				training.putAll(trainings);// add all examples to training set
				//
				// System.out.println(" Hash Map" + trainings);
				if (training.size() < testing.size())// if training set has less
														// examples than testing
														// size
				{
					HashMap bcc = new HashMap();// store remaining examples left
					for (int iterator = 0; iterator < y; iterator++) {
						bcc.put(iterator, x1x2lists.get(iterator));
						training.putAll(bcc);// store remaining examples in
												// training set
					}
				}
				// System.out.println(" Training " + training);
			} else {
				for (int i = count; i < x1x2lists.size(); i++) {
					training.put(i, x1x2lists.get(i));// store the examples in
														// training
				}
				// System.out.println("Training "+ training);
			}
			if (training.size() + testing.size() < x1x2lists.size())// if still
																	// examples
																	// are left
			{
				List a = new ArrayList<>();
				a.add(x1x2lists.get(x1x2lists.size() - 1));
				// System.out.println(a);
				testing.addAll(a);// add remaining elements to the testing set
				// System.out.println("Testing" + testing);
			}
			// System.out.println("Testing set" + testing);
			// System.out.println("Training set" + training);
			int distancearray[] = new int[x1x2lists.size()];// array to store the
															// distances
			if (k == 1) {
				for (int i = 0; i < testing.size(); i++) {
					String testingexample = (String) testing.get(i);// get the
																	// example
																	// number
																	// and sign
					// System.out.println(testingexample);
					String testx1 = (String) testingexample.subSequence(0, 1);// get
																				// x1
					String testx2 = (String) testingexample.subSequence(1, 2);// get
																				// x2
					Set trainingset = training.keySet();
					String j = trainingset.toString();// get examplenumber set
					// System.out.println(j);
					int ab = Integer.parseInt(j.substring(1, 2));// get the
																	// current
																	// example
																	// number
					// System.out.println("ab" + ab);
					int temps = j.length();
					int bc = Integer
							.parseInt(j.substring(temps - 2, temps - 1));// get
																			// the
																			// example
																			// number
																			// at
																			// highest
																			// index
					// System.out.println("bc" + bc);
					int variable = 0;// iterator for distance array
					for (int l = ab; l <= bc; l++) {
						String trainingexample = (String) training.get(l);// get
																			// the
																			// current
																			// training
																			// example
						// System.out.println(trainingexample);
						String trainx1 = (String) trainingexample.subSequence(
								0, 1);// get x1
						String trainx2 = (String) trainingexample.subSequence(
								1, 2);// get x2
						// calculate distance between testing example and
						// training example
						int distance = (int) (Math.pow(
								((Integer.valueOf(testx1)) - (Integer
										.valueOf(trainx1))), 2) + Math.pow(
								((Integer.valueOf(testx2)) - (Integer
										.valueOf(trainx2))), 2));// calculate
																	// the
																	// distance
																	// between
																	// the x1
																	// and x2 of
																	// training
																	// and
																	// testing
																	// examples
						distancearray[variable] = distance;// store the distance
															// in the distance
															// array
						// System.out.println("Distance btwn" + testingexample+
						// "and" + trainingexample + " " +
						// distancearray[variable]);
						variable++;// increment to the appropriate position
					}

					// find the smallest distance
					int smallest = Integer.MAX_VALUE;
					int smallestindex = ab;
					for (int l = 0; l < variable; l++) {
						if (smallest > distancearray[l]) {
							// System.out.println("Distance element" +
							// distancearray[k]);
							smallest = distancearray[l];
							smallestindex = ab + l;
						}
					}

					// System.out.println("Smallest" + smallest);
					// System.out.println(smallestindex);
					// retrieving the sign of the example with smallest distance
					String trainingsign = (String) ((String) training
							.get(smallestindex)).subSequence(2, 3);
					// System.out.println(trainings.get(smallestindex) +
					// trainingsign);
					String temp = (String) testing.get(i);// temporary variable
															// to store x1, x2
															// and sign
					String testingsign = (String) temp.subSequence(2, 3); // retrieving
																			// the
																			// sign
																			// of
																			// the
																			// testing
																			// example
					// System.out.println("Testing singns" + testingsign);
					if (testingsign.equals(trainingsign))// if testing and
															// training signs
															// are same
					{
						// do nothing
						// System.out.println("Signs are same");
						testanswers.put((i), testingsign);// store the test
															// answers for each
															// example
						// System.out.println("Test answers for each test" +
						// testanswers);
					} else// if testing and training signs are different
					{
						// System.out.println("Signs are not same" +
						// testingsign);
						String testex = temp.substring(0, 2);
						testex = testex + testingsign;
						// System.out.println(testing.get(i));
						testing.set(i, testex);
						// System.out.println(testing.get(i));
						testanswers.put((i), testingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					}
				}
			}

			if (k == 2) {
				for (int i = 0; i < testing.size(); i++) {
					String testingexample = (String) testing.get(i);
					// System.out.println(testingexample);
					String testx1 = (String) testingexample.subSequence(0, 1); // get
																				// x1
																				// and
																				// x2
					String testx2 = (String) testingexample.subSequence(1, 2);
					Set trainingset = training.keySet();
					String j = trainingset.toString();
					// System.out.println(j);
					int ab = Integer.parseInt(j.substring(1, 2));
					// System.out.println("ab" + ab);
					int temps = j.length();
					int bc = Integer
							.parseInt(j.substring(temps - 2, temps - 1));
					// System.out.println("bc" + bc);
					int variable = 0;
					for (int l = ab; l <= bc; l++) {
						if(training.get(l)!=null)
						{
						String trainingexample = (String) training.get(l);
						// System.out.println(trainingexample);
						String trainx1 = (String) trainingexample.subSequence(
								0, 1); // get x1 and x2
						String trainx2 = (String) trainingexample.subSequence(
								1, 2);
						// calculate distance between testing example and
						// training example
						int distance = (int) (Math.pow(
								((Integer.valueOf(testx1)) - (Integer
										.valueOf(trainx1))), 2) + Math.pow(
								((Integer.valueOf(testx2)) - (Integer
										.valueOf(trainx2))), 2));
						distancearray[variable] = distance;
						// System.out.println("Distance btwn" + testingexample+
						// "and" + trainingexample + " " +
						// distancearray[variable]);
						variable++;
					}

					int min1 = distancearray[0];// variable to store minimum
												// distance
					int m1index = 0;// index of minimum distance
					int min2 = distancearray[1];// variable to store next
												// minimum distance
					int m2index = 1;// index of next minimum distance
					if (min2 < min1) {
						min1 = distancearray[1];
						m1index = 1;
						min2 = distancearray[0];
						m2index = 0;
					}
					for (int p = 2; p < distancearray.length; p++) {
						if (distancearray[p] <= min1) {
							min2 = min1;
							m2index = m1index;
							min1 = distancearray[p];
							m1index = p;
						} else if (distancearray[p] < min2) {
							min2 = distancearray[p];
							m2index = p;
						}
					}
					// Arrays.sort(distancearray);
					int positivecount = 0;
					int negativecount = 0;
					String finaltrainingsign = " ";
					// System.out.println(distancearray[0]);
					// System.out.println(distancearray[1]);
					// System.out.println("Smallest" + smallest);
					// retrieving the sign of the example with smallest distance
					// System.out.println(trainings);
					// System.out.println(training);
					// System.out.println("ab"+ab);
					// System.out.println("m1"+min1 + "index" + m1index);
					// System.out.println("m2"+min2 + "index" + m2index);
					if(training.get(ab+m1index)!=null && training.get(ab+m2index)!=null)
					{
					String trainingsign = (String) ((String) training.get(ab
							+ m1index)).subSequence(2, 3);
					// System.out.println("Trainingsign"+trainingsign);
					String trainingsign2 = (String) ((String) training.get(ab
							+ m2index)).subSequence(2, 3);
					// System.out.println("Trainingsign2"+trainingsign2);
					if (trainingsign.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign2.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					// System.out.println("PositiveCount" + positivecount);
					// System.out.println("Negativecount" + negativecount);
					if (positivecount > negativecount) {
						finaltrainingsign = "+";
					} else if (positivecount < negativecount) {
						finaltrainingsign = "-";
					} else {
						finaltrainingsign = "-";
					}
					// System.out.println("Final Training Sign" +
					// finaltrainingsign);
					// System.out.println(trainings.get(smallestindex) +
					// trainingsign);
					String temp = (String) testing.get(i);// temporary variable
															// to store x1, x2
															// and sign
					String testingsign = (String) temp.subSequence(2, 3); // retrieving
																			// the
																			// sign
																			// of
																			// the
																			// testing
																			// example
					// System.out.println("Testing singns" + testingsign);
					if (testingsign.equals(finaltrainingsign)) {
						// do nothing
						// System.out.println("Signs are same");
						testanswers.put((z + i), testingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					} else {
						// System.out.println("Signs are not same" +
						// testingsign);
						// String testex = temp.substring(0, 2);
						// System.out.println(testex);
						// testex=testex+testingsign;
						// System.out.println(testex);
						// // System.out.println(testing.get(i));
						// testing.set(i, testex);
						// System.out.println(testing.get(i));
						testanswers.put((z + i), finaltrainingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					}
					}
					}
					}
			}
			if (k == 3) {
				for (int i = 0; i < testing.size(); i++) {
					String testingexample = (String) testing.get(i);
					// System.out.println(testingexample);
					String testx1 = (String) testingexample.subSequence(0, 1); // get
																				// x1
																				// and
																				// x2
					String testx2 = (String) testingexample.subSequence(1, 2);
					Set trainingset = training.keySet();
					String j = trainingset.toString();
					// System.out.println(j);
					int ab = Integer.parseInt(j.substring(1, 2));
					// System.out.println("ab" + ab);
					int temps = j.length();
					int bc = Integer
							.parseInt(j.substring(temps - 2, temps - 1));
					// System.out.println("bc" + bc);
					int variable = 0;
					for (int l = ab; l <= bc; l++) {
						if(training.get(l)!=null)
						{
						String trainingexample = (String) training.get(l);
						// System.out.println(trainingexample);
						String trainx1 = (String) trainingexample.subSequence(
								0, 1); // get x1 and x2
						String trainx2 = (String) trainingexample.subSequence(
								1, 2);
						// calculate distance between testing example and
						// training example
						int distance = (int) (Math.pow(
								((Integer.valueOf(testx1)) - (Integer
										.valueOf(trainx1))), 2) + Math.pow(
								((Integer.valueOf(testx2)) - (Integer
										.valueOf(trainx2))), 2));
						distancearray[variable] = distance;
						// System.out.println("Distance btwn" + testingexample+
						// "and" + trainingexample + " " +
						// distancearray[variable]);
						variable++;
					}
					int distancearraycopy[] = new int[distancearray.length];
					for (int kl = 0; kl < distancearraycopy.length; kl++) {
						distancearraycopy[kl] = distancearray[kl];
					}

					Arrays.sort(distancearraycopy);
					int min1 = distancearraycopy[0];
					int min2 = distancearraycopy[1];
					int min3 = distancearraycopy[2];
					// System.out.println("min1" + );
					int m1index = 0;
					int m2index = 0;
					int m3index = 0;
					for (int index = 0; index < distancearray.length; index++) {
						if (distancearray[index] == min1)
							m1index = index;
						if (distancearray[index] == min2)
							m2index = index;
						if (distancearray[index] == min3)
							m3index = index;
					}
					int positivecount = 0;
					int negativecount = 0;
					String finaltrainingsign = " ";
					// retrieving the sign of the example with smallest distance
					// System.out.println(trainings);
					// System.out.println(training);
					// System.out.println("ab"+ab);
					// System.out.println("m1"+min1 + "index" + m1index);
					// System.out.println("m2"+min2 + "index" + m2index);
					// System.out.println("m3"+min3 + "index" + m3index);
					if(training.get(ab+m1index)!=null&&training.get(ab+m2index)!=null&&training.get(ab+m3index)!=null)
					{
					String trainingsign = (String) ((String) training.get(ab
							+ m1index)).subSequence(2, 3);
					// System.out.println("Trainingsign"+trainingsign);
					String trainingsign2 = (String) ((String) training.get(ab
							+ m2index)).subSequence(2, 3);
					// System.out.println("Trainingsign2"+trainingsign2);
					String trainingsign3 = (String) ((String) training.get(ab
							+ m3index)).subSequence(2, 3);
					// System.out.println("Trainingsign3"+trainingsign3);
					if (trainingsign.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign2.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign3.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					// System.out.println("PositiveCount" + positivecount);
					// System.out.println("Negativecount" + negativecount);
					finaltrainingsign = "+";
					if (positivecount > negativecount) {
					} else if (positivecount < negativecount) {
						finaltrainingsign = "-";
					} else {
						finaltrainingsign = "-";
					}
					// System.out.println("Final Training Sign" +
					// finaltrainingsign);
					// System.out.println(trainings.get(smallestindex) +
					// trainingsign);
					String temp = (String) testing.get(i);// temporary variable
															// to store x1, x2
															// and sign
					String testingsign = (String) temp.subSequence(2, 3); // retrieving
																			// the
																			// sign
																			// of
																			// the
																			// testing
																			// example
					// System.out.println("Testing singns" + testingsign);
					if (testingsign.equals(finaltrainingsign)) {
						// do nothing
						// System.out.println("Signs are same");
						testanswers.put((z + i), testingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					} else {
						// System.out.println("Signs are not same" +
						// testingsign);
						// String testex = temp.substring(0, 2);
						// System.out.println(testex);
						// testex=testex+testingsign;
						// System.out.println(testex);
						// // System.out.println(testing.get(i));
						// testing.set(i, testex);
						// System.out.println(testing.get(i));
						testanswers.put((z + i), finaltrainingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					}
					}
					}
				}
			}
			if (k == 4) {
				for (int i = 0; i < testing.size(); i++) {
					String testingexample = (String) testing.get(i);
					// System.out.println(testingexample);
					String testx1 = (String) testingexample.subSequence(0, 1); // get
																				// x1
																				// and
																				// x2
					String testx2 = (String) testingexample.subSequence(1, 2);
					Set trainingset = training.keySet();
					String j = trainingset.toString();
					// System.out.println(j);
					int ab = Integer.parseInt(j.substring(1, 2));
					// System.out.println("ab" + ab);
					int temps = j.length();
					int bc = Integer
							.parseInt(j.substring(temps - 2, temps - 1));
					// System.out.println("bc" + bc);
					int variable = 0;
					for (int l = ab; l <= bc; l++) {
						if(training.get(l)!=null)
						{
						String trainingexample = (String) training.get(l);
						// System.out.println(trainingexample);
						String trainx1 = (String) trainingexample.subSequence(
								0, 1); // get x1 and x2
						String trainx2 = (String) trainingexample.subSequence(
								1, 2);
						// calculate distance between testing example and
						// training example
						int distance = (int) (Math.pow(
								((Integer.valueOf(testx1)) - (Integer
										.valueOf(trainx1))), 2) + Math.pow(
								((Integer.valueOf(testx2)) - (Integer
										.valueOf(trainx2))), 2));
						distancearray[variable] = distance;
						// System.out.println("Distance btwn" + testingexample+
						// "and" + trainingexample + " " +
						// distancearray[variable]);
						variable++;
					}
					int distancearraycopy[] = new int[distancearray.length];
					for (int kl = 0; kl < distancearraycopy.length; kl++) {
						distancearraycopy[kl] = distancearray[kl];
					}

					Arrays.sort(distancearraycopy);
					int min1 = distancearraycopy[0];
					int min2 = distancearraycopy[1];
					int min3 = distancearraycopy[2];
					int min4 = distancearraycopy[3];
					int m1index = 0;
					int m2index = 0;
					int m3index = 0;
					int m4index = 0;
					for (int index = 0; index < distancearray.length; index++) {
						if (distancearray[index] == min1)
							m1index = index;
						if (distancearray[index] == min2)
							m2index = index;
						if (distancearray[index] == min3)
							m3index = index;
						if (distancearray[index] == min4)
							m4index = index;
					}

					int positivecount = 0;
					int negativecount = 0;
					String finaltrainingsign = " ";
					// System.out.println(distancearray[0]);
					// System.out.println(distancearray[1]);
					// System.out.println("Smallest" + smallest);
					// retrieving the sign of the example with smallest distance
					// System.out.println(trainings);
					// System.out.println(training);
					// System.out.println("ab"+ab);
					// System.out.println("m1"+min1 + "index" + m1index);
					// System.out.println("m2"+min2 + "index" + m2index);
					// System.out.println("m3"+min3 + "index" + m3index);
					if(training.get(ab+m1index)!=null&&training.get(ab+m2index)!=null&&training.get(ab+m3index)!=null&&training.get(ab+m4index)!=null)
					{
					String trainingsign = (String) ((String) training.get(ab
							+ m1index)).subSequence(2, 3);
					// System.out.println("Trainingsign"+trainingsign);
					String trainingsign2 = (String) ((String) training.get(ab
							+ m2index)).subSequence(2, 3);
					// System.out.println("Trainingsign2"+trainingsign2);
					String trainingsign3 = (String) ((String) training.get(ab
							+ m3index)).subSequence(2, 3);
					// System.out.println("Trainingsign3"+trainingsign3);
					String trainingsign4 = (String) ((String) training.get(ab
							+ m4index)).subSequence(2, 3);
					// System.out.println("Trainingsign4"+trainingsign4);
					if (trainingsign.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign2.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign3.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign4.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					// System.out.println("PositiveCount" + positivecount);
					// System.out.println("Negativecount" + negativecount);
					if (positivecount > negativecount) {
						finaltrainingsign = "+";
					} else if (positivecount < negativecount) {
						finaltrainingsign = "-";
					} else {
						finaltrainingsign = "-";
					}
					// System.out.println("Final Training Sign" +
					// finaltrainingsign);
					// System.out.println(trainings.get(smallestindex) +
					// trainingsign);
					String temp = (String) testing.get(i);// temporary variable
															// to store x1, x2
															// and sign
					String testingsign = (String) temp.subSequence(2, 3); // retrieving
																			// the
																			// sign
																			// of
																			// the
																			// testing
																			// example
					// System.out.println("Testing singns" + testingsign);
					if (testingsign.equals(finaltrainingsign)) {
						// do nothing
						// System.out.println("Signs are same");
						testanswers.put((z + i), testingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					} else {
						// System.out.println("Signs are not same" +
						// testingsign);
						// String testex = temp.substring(0, 2);
						// System.out.println(testex);
						// testex=testex+testingsign;
						// System.out.println(testex);
						// // System.out.println(testing.get(i));
						// testing.set(i, testex);
						// System.out.println(testing.get(i));
						testanswers.put((z + i), finaltrainingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					}
					}
					}
				}
			}
			if (k == 5) {
				for (int i = 0; i < testing.size(); i++) {
					String testingexample = (String) testing.get(i);
					// System.out.println(testingexample);
					String testx1 = (String) testingexample.subSequence(0, 1); // get
																				// x1
																				// and
																				// x2
					String testx2 = (String) testingexample.subSequence(1, 2);
					Set trainingset = training.keySet();
					String j = trainingset.toString();
					// System.out.println(j);
					int ab = Integer.parseInt(j.substring(1, 2));
					// System.out.println("ab" + ab);
					int temps = j.length();
					int bc = Integer
							.parseInt(j.substring(temps - 2, temps - 1));
					// System.out.println("bc" + bc);
					int variable = 0;
					for (int l = ab; l <= bc; l++) {
						if(training.get(l)!=null)
						{
						String trainingexample = (String) training.get(l);
						// System.out.println(trainingexample);
						String trainx1 = (String) trainingexample.subSequence(
								0, 1); // get x1 and x2
						String trainx2 = (String) trainingexample.subSequence(
								1, 2);
						// calculate distance between testing example and
						// training example
						int distance = (int) (Math.pow(
								((Integer.valueOf(testx1)) - (Integer
										.valueOf(trainx1))), 2) + Math.pow(
								((Integer.valueOf(testx2)) - (Integer
										.valueOf(trainx2))), 2));
						distancearray[variable] = distance;
						// System.out.println("Distance btwn" + testingexample+
						// "and" + trainingexample + " " +
						// distancearray[variable]);
						variable++;
					}
					int distancearraycopy[] = new int[distancearray.length];
					for (int kl = 0; kl < distancearraycopy.length; kl++) {
						distancearraycopy[kl] = distancearray[kl];
					}
					if (distancearraycopy.length < 5) {
						return 0;
					}
					Arrays.sort(distancearraycopy);
					int min1 = distancearraycopy[0];
					int min2 = distancearraycopy[1];
					int min3 = distancearraycopy[2];
					int min4 = distancearraycopy[3];
					int min5 = distancearraycopy[4];
					int m1index = 0;
					int m2index = 0;
					int m3index = 0;
					int m4index = 0;
					int m5index = 0;
					for (int index = 0; index < distancearray.length; index++) {
						if (distancearray[index] == min1)
							m1index = index;
						if (distancearray[index] == min2)
							m2index = index;
						if (distancearray[index] == min3)
							m3index = index;
						if (distancearray[index] == min4)
							m4index = index;
						if (distancearray[index] == min5)
							m5index = index;
					}

					int positivecount = 0;
					int negativecount = 0;
					String finaltrainingsign = " ";
					// System.out.println(distancearray[0]);
					// System.out.println(distancearray[1]);
					// System.out.println("Smallest" + smallest);
					// retrieving the sign of the example with smallest distance
					// System.out.println(trainings);
					// System.out.println(training);
					// System.out.println("ab"+ab);
					// System.out.println("m1"+min1 + "index" + m1index);
					// System.out.println("m2"+min2 + "index" + m2index);
					// System.out.println("m3"+min3 + "index" + m3index);
					if(training.get(ab+m1index)!=null&&training.get(ab+m2index)!=null&&training.get(ab+m3index)!=null&&training.get(ab+m4index)!=null&&training.get(ab+m5index)!=null)
					{
					String trainingsign = (String) ((String) training.get(ab
							+ m1index)).subSequence(2, 3);
					// System.out.println("Trainingsign"+trainingsign);
					String trainingsign2 = (String) ((String) training.get(ab
							+ m2index)).subSequence(2, 3);
					// System.out.println("Trainingsign2"+trainingsign2);
					String trainingsign3 = (String) ((String) training.get(ab
							+ m3index)).subSequence(2, 3);
					// System.out.println("Trainingsign3"+trainingsign3);
					String trainingsign4 = (String) ((String) training.get(ab
							+ m4index)).subSequence(2, 3);
					// System.out.println("Trainingsign4"+trainingsign4);
					String trainingsign5 = (String) ((String) training.get(ab
							+ m5index)).subSequence(2, 3);
					// System.out.println("Trainingsign5"+trainingsign5);
					if (trainingsign.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign2.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign3.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign4.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					if (trainingsign5.contains("+")) {
						positivecount++;
					} else {
						negativecount++;
					}
					// System.out.println("PositiveCount" + positivecount);
					// System.out.println("Negativecount" + negativecount);
					if (positivecount > negativecount) {
						finaltrainingsign = "+";
					} else if (positivecount < negativecount) {
						finaltrainingsign = "-";
					} else {
						finaltrainingsign = "-";
					}
					// System.out.println("Final Training Sign" +
					// finaltrainingsign);
					// System.out.println(trainings.get(smallestindex) +
					// trainingsign);
					String temp = (String) testing.get(i);// temporary variable
															// to store x1, x2
															// and sign
					String testingsign = (String) temp.subSequence(2, 3); // retrieving
																			// the
																			// sign
																			// of
																			// the
																			// testing
																			// example
					// System.out.println("Testing singns" + testingsign);
					if (testingsign.equals(finaltrainingsign)) {
						// do nothing
						// System.out.println("Signs are same");
						testanswers.put((z + i), testingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					} else {
						// System.out.println("Signs are not same" +
						// testingsign);
						// String testex = temp.substring(0, 2);
						// System.out.println(testex);
						// testex=testex+testingsign;
						// System.out.println(testex);
						// // System.out.println(testing.get(i));
						// testing.set(i, testex);
						// System.out.println(testing.get(i));
						testanswers.put((z + i), finaltrainingsign);
						// System.out.println("Test answers for each test" +
						// testanswers);
					}
					}
					}
				}
			}
			// System.out.println("Test answers" + testanswers);
			errors = calculateerror(testanswers);
			// System.out.println("Error" + errors);

			k++;
			z = z + x;
			count = count + x;
			y = y + x;
			times--;

		}

		return errors;

	}

	/**
	 * Function to calculate error
	 * 
	 * @param testanswers
	 * @return
	 */

	public static float calculateerror(HashMap testanswers) {
		int errorinexample = 0;
		float error, sigma;

		// System.out.println(testanswers + " test answer");
		for (int i = 0; i < testanswers.size(); i++) {
			String testanswersign = (String) testanswers.get(i);// get the test
																// answer sign
			// System.out.println("Testanswer is" + a);
			// String testexamplenumber = a.substring(0,1);
			// System.out.println("Test example number" + testexamplenumber);
			// String testanswersign = a.substring(1);
			// System.out.println(testanswersign);
			// String testexample =
			// x1x2lists.get(Integer.parseInt(testexamplenumber));
			String testexample = x1x2lists.get(i);// get the test example
			// System.out.println(testexample);
			String testexamplesign = testexample.substring(2, 3);// get the sign
																	// of test
																	// example
			// System.out.println("Test example sign" + testexamplesign);
			// System.out.println(testanswersign);
			if (!testexamplesign.equals(testanswersign)) {
				errorinexample++;// increment the error
			} else {
				// do nothing
			}

		}
		// System.out.println(testanswers.size());
		// float x = Integer.valueOf(b)/Integer.valueOf(a);
		// System.out.println(x1x2lists.size());
		// System.out.println(errorinexample);
		error = (float) errorinexample / (float) x1x2lists.size();// calculate
																	// the error
		return error;
	}

	public static void nearestneighbour(int k) {
		if (k == 1) {
			String answerarray[][] = new String[Integer.valueOf(d)][Integer
					.valueOf(e)];// array to print the matrix
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					String b = Integer.toString(j);
					String a = Integer.toString(m);
					// System.out.println(a);
					// System.out.println(b);
					String c = a + b;// get the example number
					// System.out.println(c + vars.get(c));
					if (vars.get(c) != null)// if hashmap doesnt return null
											// value
					{
						String d = String.valueOf(Integer.parseInt(a) + 1);
						String e = String.valueOf(Integer.parseInt(b) + 1);
						String f = String.valueOf(Integer.parseInt(a) - 1);
						String g = String.valueOf(Integer.parseInt(b) - 1);
						String h = d + b;// right
						String i = a + e;// down
						String n = f + b;// left
						String l = a + g;// top
						n = n.replace("-", "");// if n is negative remove the
												// negative sign
						l = l.replace("-", "");// if l is negative remove the
												// negative sign
						// System.out.println("h0" + h.charAt(0) + "e0" +
						// GradedHomework1.e.charAt(0));
						if (h.charAt(0) == GradedHomework1.e.charAt(0))// if h
																		// has
																		// exceeded
																		// the
																		// index
																		// of
																		// matrix
						{
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							h = h.replace(h.charAt(0), s.charAt(0));

						}
						if (e.charAt(0) == GradedHomework1.e.charAt(0))// if e
																		// has
																		// exceeded
																		// the
																		// index
																		// of
																		// matrix
						{
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							e = e.replace(e.charAt(0), s.charAt(0));
						}
						if (i.charAt(1) == GradedHomework1.d.charAt(0))// if i
																		// has
																		// exceeded
																		// the
																		// index
																		// of
																		// matrix
						{
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							i = i.replace(i.charAt(1), s.charAt(0));
						}
						if (l.charAt(1) == GradedHomework1.d.charAt(0))// if l
																		// has
																		// exceeded
																		// the
																		// index
																		// of
																		// matrix
						{
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							l = l.replace(l.charAt(1), s.charAt(0));
						}
						// System.out.println("h" + h + "i" + i + "n" + n +"l" +
						// l);
						// System.out.println(vars.get(h));
						// System.out.println(vars.get(i));
						// System.out.println(vars.get(n));
						// System.out.println(vars.get(l));
						if (vars.get(h) == '-' || vars.get(i) == '-'
								|| vars.get(n) == '-' || vars.get(l) == '-')// if
																			// any
																			// of
																			// the
																			// 1st
																			// nearest
																			// neighbours
																			// are
																			// negative
																			// classify
																			// as
																			// negative
						{
							answerarray[j][m] = "-";
						}
						if ((vars.get(h) == '+' && vars.get(i) == '.')
								|| (vars.get(i) == '+' && vars.get(h) == '.'))// if
																				// the
																				// right
																				// and
																				// bottom
																				// nearest
																				// neighbours
																				// are
																				// .
																				// or
																				// +
																				// classify
																				// as
																				// positive
						{
							answerarray[j][m] = "+";
						}
						if ((vars.get(h) == '-' && vars.get(i) == '.')
								|| (vars.get(i) == '-' && vars.get(h) == '.'))// if
																				// the
																				// right
																				// and
																				// bottom
																				// nearest
																				// neighbours
																				// are
																				// .
																				// or
																				// -
																				// classify
																				// as
																				// negative
						{
							answerarray[j][m] = "-";
						}
						if ((vars.get(n) == '+' && vars.get(l) == '.')
								|| (vars.get(l) == '+' && vars.get(n) == '.'))// if
																				// the
																				// left
																				// and
																				// top
																				// nearest
																				// neighbours
																				// are
																				// +
																				// or
																				// .
																				// classify
																				// as
																				// positive
						{
							answerarray[j][m] = "+";
						}
						if ((vars.get(n) == '-' && vars.get(l) == '.')
								|| (vars.get(l) == '-' && vars.get(n) == '.'))// if
																				// the
																				// left
																				// and
																				// top
																				// nearest
																				// neighbours
																				// are
																				// -
																				// or
																				// .
																				// classify
																				// as
																				// negative
						{
							answerarray[j][m] = "-";
						}

					}
				}
			}
			// print the matrix
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					System.out.print(answerarray[j][m]);
				}
				System.out.println();
			}
		}
		if (k == 2) {
			String answerarray[][] = new String[Integer.valueOf(d)][Integer
					.valueOf(e)];
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					String b = Integer.toString(j);
					String a = Integer.toString(m);
					// System.out.println(a);
					// System.out.println(b);
					String c = a + b;
					// System.out.println(c + vars.get(c));
					if (vars.get(c) != null) {
						String d = String.valueOf(Integer.parseInt(a) + 1);
						String e = String.valueOf(Integer.parseInt(b) + 1);
						String f = String.valueOf(Integer.parseInt(a) - 1);
						String g = String.valueOf(Integer.parseInt(b) - 1);
						String h = d + b;// right
						String i = a + e;// down
						String n = f + b;// left
						String l = a + g;// top

						n = n.replace("-", "");
						l = l.replace("-", "");
						// System.out.println("h0" + h.charAt(0) + "e0" +
						// GradedHomework1.e.charAt(0));
						if (h.charAt(0) == GradedHomework1.e.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							h = h.replace(h.charAt(0), s.charAt(0));

						}
						if (e.charAt(0) == GradedHomework1.e.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							e = e.replace(e.charAt(0), s.charAt(0));
						}
						if (i.charAt(1) == GradedHomework1.d.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							i = i.replace(i.charAt(1), s.charAt(0));
						}
						if (l.charAt(1) == GradedHomework1.d.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							l = l.replace(l.charAt(1), s.charAt(0));
						}
						// System.out.println("h" + h + "i" + i + "n" + n +"l" +
						// l);
						// System.out.println(vars.get(h));
						// System.out.println(vars.get(i));
						// System.out.println(vars.get(n));
						// System.out.println(vars.get(l));
						if (vars.get(h) == '-' || vars.get(i) == '-'
								|| vars.get(n) == '-' || vars.get(l) == '-')// if
																			// any
																			// of
																			// the
																			// first
																			// nearest
																			// neighbours
																			// are
																			// negative
						{
							answerarray[j][m] = "-";
						}
						if ((vars.get(h) == '+' && vars.get(i) == '.')
								|| (vars.get(i) == '+' && vars.get(h) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(h) == '-' && vars.get(i) == '.')
								|| (vars.get(i) == '-' && vars.get(h) == '.')) {
							answerarray[j][m] = "-";
						}
						if ((vars.get(n) == '+' && vars.get(l) == '.')
								|| (vars.get(l) == '+' && vars.get(n) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(n) == '-' && vars.get(l) == '.')
								|| (vars.get(l) == '-' && vars.get(n) == '.')) {
							answerarray[j][m] = "-";
						}
						// left right top down
						if ((vars.get(n) == '+' && vars.get(h) == '-')
								|| (vars.get(n) == '-' && vars.get(h) == '+')
								|| (vars.get(l) == '+' && vars.get(i) == '-')
								|| (vars.get(l) == '+' && vars.get(l) == '-')) {
							answerarray[j][m] = "-";
						}

					}
				}
			}

			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					System.out.print(answerarray[j][m]);
				}
				System.out.println();
			}
		}
		if (k == 3) {
			String answerarray[][] = new String[Integer.valueOf(d)][Integer
					.valueOf(e)];
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					String b = Integer.toString(j);
					String a = Integer.toString(m);
					// System.out.println(a);
					// System.out.println(b);
					String c = a + b;
					// System.out.println(c + vars.get(c));
					if (vars.get(c) != null) {
						String d = String.valueOf(Integer.parseInt(a) + 1);
						String e = String.valueOf(Integer.parseInt(b) + 1);
						String f = String.valueOf(Integer.parseInt(a) - 1);
						String g = String.valueOf(Integer.parseInt(b) - 1);
						String h = d + b;// right
						String i = a + e;// down
						String n = f + b;// left
						String l = a + g;// top
						n = n.replace("-", "");
						l = l.replace("-", "");
						// System.out.println("h0" + h.charAt(0) + "e0" +
						// GradedHomework1.e.charAt(0));
						if (h.charAt(0) == GradedHomework1.e.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							h = h.replace(h.charAt(0), s.charAt(0));

						}

						if (e.charAt(0) == GradedHomework1.e.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							e = e.replace(e.charAt(0), s.charAt(0));
						}
						if (i.charAt(1) == GradedHomework1.d.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							i = i.replace(i.charAt(1), s.charAt(0));
						}

						if (l.charAt(1) == GradedHomework1.d.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							l = l.replace(l.charAt(1), s.charAt(0));
						}
						String o = String.valueOf(Integer.parseInt(h) + 10);// 2nd
																			// on
																			// the
																			// right
						String p = String.valueOf(Integer.parseInt(n) + 10);// 2nd
																			// on
																			// the
																			// left
						String q;
						if (i.charAt(0) == '0') {
							q = "0" + String.valueOf(Integer.parseInt(i) + 1);// 2nd
																				// on
																				// the
																				// bottom
						} else
							q = String.valueOf(Integer.parseInt(i) + 1);// 2nd
																		// on
																		// the
																		// bottom
						String r;
						if (l.charAt(0) == '0') {
							r = "0" + String.valueOf(Integer.parseInt(l) + 1);// 2nd
																				// on
																				// the
																				// top
						} else
							r = String.valueOf(Integer.parseInt(l) + 1);// 2nd
																		// on
																		// the
																		// top
						if (o.charAt(0) == GradedHomework1.e.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							o = o.replace(o.charAt(0), s.charAt(0));

						}
						if (q.charAt(1) == GradedHomework1.d.charAt(0)) {
							String s = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							q = q.replace(q.charAt(1), s.charAt(0));

						}
						// System.out.println("h" + h + "i" + i + "n" + n +"l" +
						// l);
						int pc = 0, nc = 0;

						if (vars.get(h) == '-' || vars.get(i) == '-'
								|| vars.get(n) == '-' || vars.get(l) == '-') {
							nc++;
						}
						if (vars.get(h) == '+' || vars.get(i) == '+'
								|| vars.get(n) == '+' || vars.get(l) == '+') {
							pc++;
						}
						//
						// System.out.println(vars.get(o));
						// System.out.println(vars.get(p));
						// System.out.println(vars.get(q));
						// System.out.println(vars.get(r));
						if (vars.get(o) == '+' || vars.get(p) == '+'
								|| vars.get(q) == '+' || vars.get(r) == '+') {
							pc++;
						}
						if (vars.get(o) == '-' || vars.get(p) == '-'
								|| vars.get(q) == '-' || vars.get(r) == '-') {
							nc++;
						}
						if ((vars.get(h) == '+' && vars.get(i) == '.')
								|| (vars.get(i) == '+' && vars.get(h) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(h) == '-' && vars.get(i) == '.')
								|| (vars.get(i) == '-' && vars.get(h) == '.')) {
							answerarray[j][m] = "-";
						}
						if ((vars.get(n) == '+' && vars.get(l) == '.')
								|| (vars.get(l) == '+' && vars.get(n) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(n) == '-' && vars.get(l) == '.')
								|| (vars.get(l) == '-' && vars.get(n) == '.')) {
							answerarray[j][m] = "-";
						}
						// left right top down
						if ((vars.get(n) == '+' && vars.get(h) == '-')
								|| (vars.get(n) == '-' && vars.get(h) == '+')
								|| (vars.get(l) == '+' && vars.get(i) == '-')
								|| (vars.get(l) == '+' && vars.get(l) == '-')) {
							answerarray[j][m] = "-";
						}
						if (pc <= nc) {
							answerarray[j][m] = "-";
						} else
							answerarray[j][m] = "+";
					}
				}
			}

			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					System.out.print(answerarray[j][m]);
				}
				System.out.println();
			}
		}
		if (k == 4) {
			String answerarray[][] = new String[Integer.valueOf(d)][Integer
					.valueOf(e)];
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					String b = Integer.toString(j);
					String a = Integer.toString(m);
					// System.out.println(a);
					// System.out.println(b);
					String c = a + b;
					// System.out.println(c + vars.get(c));
					if (vars.get(c) != null) {
						String d = String.valueOf(Integer.parseInt(a) + 1);
						String e = String.valueOf(Integer.parseInt(b) + 1);
						String f = String.valueOf(Integer.parseInt(a) - 1);
						String g = String.valueOf(Integer.parseInt(b) - 1);
						String h = d + b;// right
						String i = a + e;// down
						String n = f + b;// left
						String l = a + g;// top
						n = n.replace("-", "");
						l = l.replace("-", "");
						// System.out.println("h0" + h.charAt(0) + "e0" +
						// GradedHomework1.e.charAt(0));
						if (h.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							h = h.replace(h.charAt(0), sa.charAt(0));

						}

						if (e.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							e = e.replace(e.charAt(0), sa.charAt(0));
						}
						if (i.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							i = i.replace(i.charAt(1), sa.charAt(0));
						}

						if (l.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							l = l.replace(l.charAt(1), sa.charAt(0));
						}
						String o = String.valueOf(Integer.parseInt(h) + 10);// 2nd
																			// on
																			// the
																			// right
						String p = String.valueOf(Integer.parseInt(n) + 10);// 2nd
																			// on
																			// the
																			// left
						String q;
						if (i.charAt(0) == '0') {
							q = "0" + String.valueOf(Integer.parseInt(i) + 1);// 2nd
																				// on
																				// the
																				// bottom
						} else
							q = String.valueOf(Integer.parseInt(i) + 1);// 2nd
																		// on
																		// the
																		// bottom
						String r;
						if (l.charAt(0) == '0') {
							r = "0" + String.valueOf(Integer.parseInt(l) + 1);// 2nd
																				// on
																				// the
																				// top
						} else
							r = String.valueOf(Integer.parseInt(l) + 1);// 2nd
																		// on
																		// the
																		// top
						if (o.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							o = o.replace(o.charAt(0), sa.charAt(0));

						}
						if (q.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							q = q.replace(q.charAt(1), sa.charAt(0));

						}
						// System.out.println("h" + h + "i" + i + "n" + n +"l" +
						// l);
						int pc = 0, nc = 0;
						String s = String.valueOf(Integer.parseInt(o) + 10);// 3rd
																			// on
																			// the
																			// right
						if (s.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							s = s.replace(s.charAt(0), sa.charAt(0));

						}
						String t = String.valueOf(Integer.parseInt(p) + 10);// 3rd
																			// on
																			// the
																			// left
						if (t.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							t = t.replace(t.charAt(0), sa.charAt(0));

						}
						String u;
						if (q.charAt(0) == '0') {
							u = "0" + String.valueOf(Integer.parseInt(q) + 1);// 3rd
																				// on
																				// the
																				// bottom
						} else
							u = String.valueOf(Integer.parseInt(q) + 1);// 3rd
																		// on
																		// the
																		// bottom
						if (u.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							u = u.replace(u.charAt(1), sa.charAt(0));

						}

						String v;
						if (r.charAt(0) == '0') {
							v = "0" + String.valueOf(Integer.parseInt(r) + 1);// 3rd
																				// on
																				// the
																				// top
						} else
							v = String.valueOf(Integer.parseInt(r) + 1);// 3rd
																		// on
																		// the
																		// top
						if (v.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							v = v.replace(v.charAt(1), sa.charAt(0));

						}
						if (vars.get(h) == '-' || vars.get(i) == '-'
								|| vars.get(n) == '-' || vars.get(l) == '-') {
							nc++;
						}
						if (vars.get(h) == '+' || vars.get(i) == '+'
								|| vars.get(n) == '+' || vars.get(l) == '+') {
							pc++;
						}

						// System.out.println(vars.get(o));
						// System.out.println(vars.get(p));
						// System.out.println(vars.get(q));
						// System.out.println(vars.get(r));
						if (vars.get(s) == '+' || vars.get(t) == '+'
								|| vars.get(u) == '+' || vars.get(v) == '+') {
							pc++;
						}
						if (vars.get(s) == '-' || vars.get(t) == '-'
								|| vars.get(u) == '-' || vars.get(v) == '-') {
							nc++;
						}

						if (vars.get(o) == '+' || vars.get(p) == '+'
								|| vars.get(q) == '+' || vars.get(r) == '+') {
							pc++;
						}
						if (vars.get(o) == '-' || vars.get(p) == '-'
								|| vars.get(q) == '-' || vars.get(r) == '-') {
							nc++;
						}

						if ((vars.get(h) == '+' && vars.get(i) == '.')
								|| (vars.get(i) == '+' && vars.get(h) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(h) == '-' && vars.get(i) == '.')
								|| (vars.get(i) == '-' && vars.get(h) == '.')) {
							answerarray[j][m] = "-";
						}
						if ((vars.get(n) == '+' && vars.get(l) == '.')
								|| (vars.get(l) == '+' && vars.get(n) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(n) == '-' && vars.get(l) == '.')
								|| (vars.get(l) == '-' && vars.get(n) == '.')) {
							answerarray[j][m] = "-";
						}

						// left right top down
						if ((vars.get(n) == '+' && vars.get(h) == '-')
								|| (vars.get(n) == '-' && vars.get(h) == '+')
								|| (vars.get(l) == '+' && vars.get(i) == '-')
								|| (vars.get(l) == '+' && vars.get(l) == '-')) {
							answerarray[j][m] = "-";
						}
						if (pc <= nc) {
							answerarray[j][m] = "-";
						} else
							answerarray[j][m] = "+";
					}
				}
			}

			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					System.out.print(answerarray[j][m]);
				}
				System.out.println();
			}
		}
		if (k == 5) {
			String answerarray[][] = new String[Integer.valueOf(d)][Integer
					.valueOf(e)];
			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					String b = Integer.toString(j);
					String a = Integer.toString(m);
					// System.out.println(a);
					// System.out.println(b);
					String c = a + b;
					// System.out.println(c + vars.get(c));
					if (vars.get(c) != null) {
						String d = String.valueOf(Integer.parseInt(a) + 1);
						String e = String.valueOf(Integer.parseInt(b) + 1);
						String f = String.valueOf(Integer.parseInt(a) - 1);
						String g = String.valueOf(Integer.parseInt(b) - 1);
						String h = d + b;// right
						String i = a + e;// down
						String n = f + b;// left
						String l = a + g;// top
						n = n.replace("-", "");
						l = l.replace("-", "");
						// System.out.println("h0" + h.charAt(0) + "e0" +
						// GradedHomework1.e.charAt(0));
						if (h.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							h = h.replace(h.charAt(0), sa.charAt(0));

						}

						if (e.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							e = e.replace(e.charAt(0), sa.charAt(0));
						}
						if (i.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							i = i.replace(i.charAt(1), sa.charAt(0));
						}

						if (l.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							l = l.replace(l.charAt(1), sa.charAt(0));
						}
						String o = String.valueOf(Integer.parseInt(h) + 10);// 2nd
																			// on
																			// the
																			// right
						String p = String.valueOf(Integer.parseInt(n) + 10);// 2nd
																			// on
																			// the
																			// left
						String q;
						if (i.charAt(0) == '0') {
							q = "0" + String.valueOf(Integer.parseInt(i) + 1);// 2nd
																				// on
																				// the
																				// bottom
						} else
							q = String.valueOf(Integer.parseInt(i) + 1);// 2nd
																		// on
																		// the
																		// bottom
						String r;
						if (l.charAt(0) == '0') {
							r = "0" + String.valueOf(Integer.parseInt(l) + 1);// 2nd
																				// on
																				// the
																				// top
						} else
							r = String.valueOf(Integer.parseInt(l) + 1);// 2nd
																		// on
																		// the
																		// top
						if (o.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							o = o.replace(o.charAt(0), sa.charAt(0));

						}
						if (q.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							q = q.replace(q.charAt(1), sa.charAt(0));

						}
						// System.out.println("h" + h + "i" + i + "n" + n +"l" +
						// l);
						int pc = 0, nc = 0;
						String s = String.valueOf(Integer.parseInt(o) + 10);// 3rd
																			// on
																			// the
																			// right
						if (s.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							s = s.replace(s.charAt(0), sa.charAt(0));

						}
						String t = String.valueOf(Integer.parseInt(p) + 10);// 3rd
																			// on
																			// the
																			// left
						if (t.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							t = t.replace(t.charAt(0), sa.charAt(0));

						}
						String u;
						if (q.charAt(0) == '0') {
							u = "0" + String.valueOf(Integer.parseInt(q) + 1);// 3rd
																				// on
																				// the
																				// bottom
						} else
							u = String.valueOf(Integer.parseInt(q) + 1);// 3rd
																		// on
																		// the
																		// bottom
						if (u.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							u = u.replace(u.charAt(1), sa.charAt(0));

						}

						String v;
						if (r.charAt(0) == '0') {
							v = "0" + String.valueOf(Integer.parseInt(r) + 1);// 3rd
																				// on
																				// the
																				// top
						} else
							v = String.valueOf(Integer.parseInt(r) + 1);// 3rd
																		// on
																		// the
																		// top
						if (v.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							v = v.replace(v.charAt(1), sa.charAt(0));

						}
						String w = String.valueOf(Integer.parseInt(s) + 10);// 4th
																			// on
																			// the
																			// right
						if (w.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							w = w.replace(w.charAt(0), sa.charAt(0));

						}

						String x = String.valueOf(Integer.parseInt(t) + 10);// 4th
																			// on
																			// the
																			// left
						if (x.charAt(0) == GradedHomework1.e.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							x = x.replace(x.charAt(0), sa.charAt(0));

						}

						String y;
						if (u.charAt(0) == '0') {
							y = "0" + String.valueOf(Integer.parseInt(u) + 1);// 3rd
																				// on
																				// the
																				// bottom
						} else
							y = String.valueOf(Integer.parseInt(u) + 1);// 3rd
																		// on
																		// the
																		// bottom
						if (y.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							y = y.replace(y.charAt(1), sa.charAt(0));

						}

						String z;
						if (v.charAt(0) == '0') {
							z = "0" + String.valueOf(Integer.parseInt(v) + 1);// 3rd
																				// on
																				// the
																				// top
						} else
							z = String.valueOf(Integer.parseInt(v) + 1);// 3rd
																		// on
																		// the
																		// top
						if (z.charAt(1) == GradedHomework1.d.charAt(0)) {
							String sa = String.valueOf(Integer.parseInt(e) - 1);
							// System.out.println("s" + s);
							z = z.replace(z.charAt(1), sa.charAt(0));

						}
						if (vars.get(h) == '-' || vars.get(i) == '-'
								|| vars.get(n) == '-' || vars.get(l) == '-') {
							nc++;
						}
						if (vars.get(h) == '+' || vars.get(i) == '+'
								|| vars.get(n) == '+' || vars.get(l) == '+') {
							pc++;
						}

						if (vars.get(o) == '+' || vars.get(p) == '+'
								|| vars.get(q) == '+' || vars.get(r) == '+') {
							pc++;
						}
						if (vars.get(o) == '-' || vars.get(p) == '-'
								|| vars.get(q) == '-' || vars.get(r) == '-') {
							nc++;
						}

						if (vars.get(s) == '+' || vars.get(t) == '+'
								|| vars.get(u) == '+' || vars.get(v) == '+') {
							pc++;
						}
						if (vars.get(s) == '-' || vars.get(t) == '-'
								|| vars.get(u) == '-' || vars.get(v) == '-') {
							nc++;
						}
						if (vars.get(w) == '+' || vars.get(x) == '+'
								|| vars.get(y) == '+' || vars.get(z) == '+') {
							pc++;
						}
						if (vars.get(w) == '-' || vars.get(x) == '-'
								|| vars.get(y) == '-' || vars.get(z) == '-') {
							nc++;
						}

						if ((vars.get(h) == '+' && vars.get(i) == '.')
								|| (vars.get(i) == '+' && vars.get(h) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(h) == '-' && vars.get(i) == '.')
								|| (vars.get(i) == '-' && vars.get(h) == '.')) {
							answerarray[j][m] = "-";
						}
						if ((vars.get(n) == '+' && vars.get(l) == '.')
								|| (vars.get(l) == '+' && vars.get(n) == '.')) {
							answerarray[j][m] = "+";
						}
						if ((vars.get(n) == '-' && vars.get(l) == '.')
								|| (vars.get(l) == '-' && vars.get(n) == '.')) {
							answerarray[j][m] = "-";
						}

						// left right top down
						if ((vars.get(n) == '+' && vars.get(h) == '-')
								|| (vars.get(n) == '-' && vars.get(h) == '+')
								|| (vars.get(l) == '+' && vars.get(i) == '-')
								|| (vars.get(l) == '+' && vars.get(l) == '-')) {
							answerarray[j][m] = "-";
						}
						if (pc <= nc) {
							answerarray[j][m] = "-";
						} else
							answerarray[j][m] = "+";
					}
				}
			}

			for (int j = 0; j < Integer.valueOf(d); j++) {
				for (int m = 0; m < Integer.valueOf(e); m++) {
					System.out.print(answerarray[j][m]);
				}
				System.out.println();
			}
		}
	}

	/**
	 * Function to read the lines of first text file
	 * 
	 * @throws IOException
	 */
	public final void linebyline() throws IOException {

		try (Scanner scanner = new Scanner(filepath, ENCODING.name())) {
			processfirstline(scanner.nextLine());// process the first line of
													// the file
			while (scanner.hasNextLine()) {
				Scanner scanners = new Scanner(scanner.nextLine());
				f = scanners.nextLine();
				var.put(q, f);// store the first line in hashmap
				q++;
			}
		}
		for (int p = 0; p < q; p++) {
			int i = 0;
			String j = var.get(p);
			StringTokenizer st = new StringTokenizer(j, " ");
			String[] array = new String[st.countTokens()];
			while (st.hasMoreElements()) {
				array[i] = st.nextToken();// store each token of the string i.e
											// each token of each line of input
				i++;
			}
			// for(int r=0; r<i; r++)
			// {
			// System.out.println(array[r]);
			// }
		}
		// System.out.println(var.get(0));
		// System.out.println(var.get(1));
		// System.out.println(var.get(2));
	}

	/**
	 * Function to process lines of 2nd input file
	 * 
	 * @throws IOException
	 */
	public final void linebylines() throws IOException {
		try (Scanner scanner = new Scanner(filepath, ENCODING.name())) {
			processfirstlines(scanner.nextLine());// process first line of 2nd
													// file
			int m = Integer.valueOf(d);
			int n = 0;

			while (scanner.hasNextLine()) {
				while (n < m) {
					Scanner scanners = new Scanner(scanner.nextLine());
					g = scanners.nextLine();// process each line of 2nd file
					resultarray = new char[g.length()][(Integer.valueOf(d)) + 1];// create
																					// array
																					// of
																					// appropriate
																					// size
					for (int p = 0; p < g.length(); p = p + 2) {
						// System.out.println("p is" + p + " n is" +n +
						// resultarray[p][n]);
						resultarray[p][n] = g.charAt(p);// store each token of
														// the array

						int q = p / 2;
						String a = Integer.toString(n);
						String b = Integer.toString(q);
						String c = b + a;
						// System.out.println(c);
						char z = resultarray[p][n];
						// System.out.println(z);
						if (z == '.') {// store the symbol in hashmap vars
							vars.put(c, z);
						} else if (z == '+' || z == '-')// store the symbol in
														// hashmap vars
						{
							vars.put(c, z);
							// System.out.println(c);
							// System.out.println(vars.get(c));
						}
					}
					n++;
				}
			}
		}
	}

	/**
	 * Function to process the first line of the 1st file
	 * 
	 * @param line
	 */
	protected void processfirstline(String line) {
		Scanner scanner = new Scanner(line);

		a = scanner.next();// read k
		b = scanner.next();// read number of examples
		c = scanner.next();// read the total number of lines to expect in the
							// input file
		// log("A is" + a + "B is" + b + "C is" + c);
	}

	/**
	 * Function to process the first line of the 2nd file
	 * 
	 * @param line
	 */
	protected void processfirstlines(String line) {
		Scanner scanner = new Scanner(line);

		d = scanner.next();// read number of rows
		e = scanner.next();// read number of columns
		// log("D is" + d + "E is" + e);
	}

	/************************* Helper Functions **********************/
	protected void processline(String line) {

		// System.out.println(var.get(0));
	}

	public static void log(Object aobject) {
		System.out.println(String.valueOf(aobject));
	}

	public String quote(String aText) {
		String QUOTE = "'";
		return QUOTE + aText + QUOTE;
	}
}