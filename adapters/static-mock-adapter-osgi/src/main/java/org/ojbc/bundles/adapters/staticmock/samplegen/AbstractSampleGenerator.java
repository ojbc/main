/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.bundles.adapters.staticmock.samplegen.staticname.vehiclecrash.VehicleCrashMatthewsSampleGenerator;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Abstract base class of all sample generators. This class contains useful pojos and methods for working with the sample (fake) identities and other
 * randomly generated content.
 * 
 */
public abstract class AbstractSampleGenerator {

	public AbstractSampleGenerator() throws ParserConfigurationException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		documentBuilder = dbf.newDocumentBuilder();
		randomGenerator = new RandomDataGenerator();
		loadStateZipCodeDataMap();
		loadVehicleData();
	}

	/**
	 * A utility structure class that represents the information returned from loading an identity from the Fake Name Generator
	 * 
	 */
	protected final class PersonElementWrapper {
		public String id;
		public String sex;
		public String title;
		public String firstName;
		public String middleName;
		public String lastName;
		public String streetAddress;
		public String city;
		public String state;
		public String zipCode;
		public String country;
		public String countryFull;
		public String emailAddress;
		public String username;
		public String password;
		public String telephoneNumber;
		public String mothersMaiden;
		public String birthday;
		public DateTime birthdate;
		public String cCType;
		public String cCNumber;
		public String cVV2;
		public String cCExpires;
		public String nationalID;
		public String uPS;
		public String occupation;
		public String company;
		public String vehicle;
		public String domain;
		public String bloodType;
		public String pounds;
		public String kilograms;
		public String feetInches;
		public String centimeters;
		public String gUID;
		public String latitude;
		public String longitude;
		String personId;
		public String telephoneAreaCode;
		public String telephoneExchange;
		public String telephoneLine;
		public String addressStreetNumber;
		public String addressStreetName;

		public PersonElementWrapper(String line, String stateParam) {

			String[] tokens = line.split(",");

			id = tokens[0];
			sex = tokens[1];
			title = tokens[2];
			firstName = tokens[3];
			middleName = tokens[4];
			lastName = tokens[5];
			streetAddress = tokens[6];
			city = tokens[7];
			state = tokens[8];
			zipCode = tokens[9];
			country = tokens[10];
			countryFull = tokens[11];
			emailAddress = tokens[12];
			username = tokens[13];
			password = tokens[14];
			telephoneNumber = tokens[15];
			mothersMaiden = tokens[16];
			birthday = tokens[17];
			cCType = tokens[18];
			cCNumber = tokens[19];
			cVV2 = tokens[20];
			cCExpires = tokens[21];
			nationalID = tokens[22];
			uPS = tokens[23];
			occupation = tokens[24];
			company = tokens[25];
			vehicle = tokens[26];
			domain = tokens[27];
			bloodType = tokens[28];
			pounds = tokens[29];
			kilograms = tokens[30];
			feetInches = tokens[31];
			centimeters = tokens[32];
			gUID = tokens[33];
			latitude = tokens[34];
			longitude = tokens[35];
			personId = "P-" + UUID.randomUUID().toString();

			DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy");
			birthdate = fmt.parseDateTime(birthday);

			String[] phoneNumberPieces = telephoneNumber.split("-");
			telephoneAreaCode = phoneNumberPieces[0];
			telephoneExchange = phoneNumberPieces[1];
			telephoneLine = phoneNumberPieces[2];

			String[] streetSplit = streetAddress.split(" ", 2);
			addressStreetNumber = streetSplit[0];
			addressStreetName = streetSplit[1];

			if (stateParam != null) {
				state = stateParam;
				List<ZipCodeData> zipList = stateZipCodeData.get(state);
				int zips = zipList.size();
				int index = randomGenerator.nextInt(0, zips - 1);
				ZipCodeData z = zipList.get(index);
				zipCode = z.zipCode;
				city = z.city;
			}

			state = convertStateCode(state);
		}
	}

	/**
	 * A data object class representing a Zip Code
	 * 
	 */
	protected static final class ZipCodeData {
		/**
		 * The zip code
		 */
		public String zipCode;
		/**
		 * The city in which the zip code exists
		 */
		public String city;
		/**
		 * The county in which the zip code exists
		 */
		public String county;
	}

	protected static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	protected RandomDataGenerator randomGenerator;
	protected DocumentBuilder documentBuilder;
	protected Map<String, List<ZipCodeData>> stateZipCodeData;
	protected List<String> vehicleMakeData;
	protected List<String> vehicleModelData;

	@SuppressWarnings("unused")
	private void loadVehicleData() throws IOException {
		vehicleMakeData = new ArrayList<String>();
		vehicleModelData = new ArrayList<String>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static-files/VehicleMakes.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), 1024 * 10);
		String line = null;
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			String code = st.nextToken();
			String value = st.nextToken();
			vehicleMakeData.add(code);
		}
		br.close();
		inputStream.close();
		inputStream = getClass().getClassLoader().getResourceAsStream("static-files/VehicleModels.txt");
		br = new BufferedReader(new InputStreamReader(inputStream), 1024 * 10);
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			String code = st.nextToken();
			String value = st.nextToken();
			vehicleModelData.add(code);
		}
		br.close();
		inputStream.close();
	}

	/**
	 * Get a random vehicle model
	 * 
	 * @return the model code
	 */
	protected final String getRandomVehicleModelCode() {
		int index = randomGenerator.nextInt(0, vehicleModelData.size() - 1);
		return vehicleModelData.get(index);
	}

	/**
	 * Get a random vehicle make
	 * 
	 * @return the make
	 */
	protected final String getRandomVehicleMakeCode() {
		int index = randomGenerator.nextInt(0, vehicleMakeData.size() - 1);
		return vehicleMakeData.get(index);
	}

	@SuppressWarnings("unused")
	private void loadStateZipCodeDataMap() throws IOException {
		stateZipCodeData = new HashMap<String, List<ZipCodeData>>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static-files/ZipCodeData.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		while ((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "\t");
			String country = st.nextToken();
			String zip = st.nextToken();
			String city = st.nextToken();
			String stateFull = st.nextToken();
			String state = st.nextToken();
			String county = st.nextToken();
			state = convertStateCode(state);
			List<ZipCodeData> zipList = stateZipCodeData.get(state);
			if (zipList == null) {
				zipList = new ArrayList<ZipCodeData>();
				stateZipCodeData.put(state, zipList);
			}
			ZipCodeData z = new ZipCodeData();
			z.city = city;
			z.county = county;
			z.zipCode = zip;
			zipList.add(z);
		}
	}

	/**
	 * Convert a state code to comply with the NIEM FBI STA code list
	 * 
	 * @param inputStateCode
	 *            the state code from the fake name file (USPS code)
	 * @return the FBI STA code value
	 */
	protected final String convertStateCode(String inputStateCode) {
		String outputStateCode = inputStateCode;
		// FBI STA code list
		if ("NE".equals(inputStateCode)) {
			outputStateCode = "NB";
		}
		return outputStateCode;
	}

	/**
	 * Generate a date that occurs before the specified date, with a random spread
	 * 
	 * @param baseDate
	 *            the date before which the generated date will occur
	 * @param meanDaysInThePast
	 *            average number of days prior to the specified date that the generated date occurs
	 * @return the generated date
	 */
	protected final DateTime generateNormalRandomDateBefore(DateTime baseDate, int meanDaysInThePast) {
		// we don't care so much about precision...
		int subtract = Math.abs((int) randomGenerator.nextGaussian(meanDaysInThePast, meanDaysInThePast * 2));
		DateTime newDate = baseDate.minusDays(subtract);
		return newDate;
	}

	/**
	 * Generates a random, uniformly distributed date between the two specified dates.
	 * 
	 * @param d1
	 *            the first date
	 * @param d2
	 *            the second date
	 * @return a random date between the two
	 */
	protected final DateTime generateUniformRandomDateBetween(DateTime d1, DateTime d2) {
		DateTime start = d1;
		DateTime end = d2;
		if (d1.isAfter(d2)) {
			start = d2;
			end = d1;
		}
		int daysBetween = Days.daysBetween(start, end).getDays();
		int r = randomGenerator.nextInt(0, daysBetween);
		return start.plusDays(r);
	}

	/**
	 * Generates a random, uniformly distributed time between the two specified date/times.
	 * 
	 * @param d1
	 *            the first datetime
	 * @param d2
	 *            the second datetime
	 * @return a random datetime between the two
	 */
	protected final DateTime generateUniformRandomTimeBetween(DateTime d1, DateTime d2) {
		DateTime start = d1;
		DateTime end = d2;
		if (d1.isAfter(d2)) {
			start = d2;
			end = d1;
		}
		int secondsBetween = Seconds.secondsBetween(start, end).getSeconds();
		int r = randomGenerator.nextInt(0, secondsBetween);
		return start.plusSeconds(r);
	}

	/**
	 * Generate a double value from the uniform distribution, evenly distributed between zero and the specified value
	 * 
	 * @param max
	 *            the maximum value to generate
	 * @return the generated value
	 */
	protected final double generateUniformDouble(double max) {
		return randomGenerator.nextUniform(0.0, max);
	}

	/**
	 * Conduct an "unfair" coin flip (you can make it fair by setting probability=.5)
	 * 
	 * @param probability
	 *            the probability of a positive outcome
	 * @return the outcome (true for positive)
	 */
	protected final boolean coinFlip(double probability) {
		return randomGenerator.nextUniform(0.0, 1.0) < probability;
	}

	/**
	 * Generate a random system identifier value
	 * 
	 * @param prefix
	 *            prefix for the identifier
	 * @param length
	 *            length of the identifier
	 * @return the identifier
	 */
	protected final String generateRandomID(String prefix, int length) {
		StringBuffer ret = new StringBuffer(prefix);
		for (int i = 0; i < length; i++) {
			ret.append(String.valueOf(randomGenerator.nextInt(0, 9)));
		}
		return ret.toString();
	}

	

	
	/**
	 * Intended to be overridden to provide static common last name sample ie "Matthews"
	 * 
	 * @param iGeneratedPerson
	 * 		A random person object - not used when returning static name
	 * @return
	 * 		A last name, either random or statically returned if implemented that way
	 */
	protected String getLastNameSample(PersonElementWrapper personElementWrapper){
	
		return personElementWrapper.lastName;
	}
	
	
	/**
	 * Intended to be overridden to provide a static common first name sample ie Joey
	 * 
	 * @param personElementWrapper
	 * 		A random person object - not used when returning static name
	 * @return
	 *  	A first name, either random or statically returned if implemented that way
	 */
	protected String getFirstNameSample(PersonElementWrapper personElementWrapper){
	
		return personElementWrapper.firstName;
	}
	
	
	/**
	 * Add multiple elements with the specified qualified name to the parent. The number of elements to add is randomly generated out of a Poisson
	 * distribution with mean meanCount.
	 * 
	 * @param parent
	 *            parent of the elements
	 * @param ns
	 *            namespace of the elements
	 * @param elementName
	 *            name of the elements
	 * @param meanCount
	 *            the mean value of the Poisson distribution to use
	 * @param ensureAtLeastOne
	 *            whether to ensure that at least one element gets added (a value of true basically just "shifts" the distribution one whole unit to
	 *            the right
	 * @return
	 */
	protected final List<Element> appendElements(Element parent, String ns, String elementName, int meanCount, boolean ensureAtLeastOne) {
		List<Element> ret = new ArrayList<Element>();
		int count = generatePoissonInt(meanCount, ensureAtLeastOne);
		for (int i = 0; i < count; i++) {
			ret.add(XmlUtils.appendElement(parent, ns, elementName));
		}
		return ret;
	}

	/**
	 * Generate an integer from the Poisson distribution, with the specified mean
	 * 
	 * @param meanCount
	 *            the mean
	 * @param ensureAtLeastOne
	 *            whether to "scale" the distribution to the right in order to ensure that a zero return is impossible
	 * @return the randomly generated value
	 */
	protected final int generatePoissonInt(double mean, boolean ensureAtLeastOne) {
		int scaleFactor = ensureAtLeastOne ? 1 : 0;
		long count = randomGenerator.nextPoisson(mean);
		if (count >= Integer.MAX_VALUE - 1) {
			// this will occur like 1 in a trillion times, given meanCounts less than 10 or so (typical values)...but still...
			count = 0;
		}
		return (int) (count + scaleFactor);
	}

	/**
	 * Append a new element with the specified name to the specified parent...maybe
	 * 
	 * @param parent
	 *            the parent
	 * @param ns
	 *            the namespace of the new element
	 * @param elementName
	 *            the name of the new element
	 * @param probability
	 *            the probability that the new element will be created
	 * @return the element, if created, or null if not
	 */
	protected final Element appendElement(Element parent, String ns, String elementName, double probability) {
		if (!coinFlip(probability)) {
			return null;
		}
		return XmlUtils.appendElement(parent, ns, elementName);
	}

	/**
	 * Generate a random value from among the values in the specified list
	 * 
	 * @param codes
	 *            the range of values
	 * @return the randomly created value
	 */
	protected final String generateRandomCodeFromList(String... codes) {
		int i = randomGenerator.nextInt(0, codes.length - 1);
		return codes[i];
	}

	/**
	 * Generate a random value from among the values in the specified list
	 * 
	 * @param list
	 *            the list of objects
	 * @return the randomly selected member of the list
	 */
	protected final Object generateRandomValueFromList(Object... list) {
		if (list.length == 1) {
			return list[0];
		}
		int i = randomGenerator.nextInt(0, list.length - 1);
		return list[i];
	}

	/**
	 * Generate a random value from among the values in the specified list
	 * 
	 * @param list
	 *            the list of objects
	 * @return the randomly selected member of the list
	 */
	protected final Object generateRandomValueFromList(List list) {
		if (list.size() == 1) {
			return list.get(0);
		}
		int i = randomGenerator.nextInt(0, list.size() - 1);
		return list.get(i);
	}
	
	
	protected String getRandomBooleanString(){
		
		boolean sampleFlag = coinFlip(.5);
		
		String sSampleFlag = String.valueOf(sampleFlag);
		
		return sSampleFlag;
	}
	
	protected String getRandomName(){
		
		PersonElementWrapper person = null;
		
		String fullName = null;
		
		try{
			person = getRandomIdentity(null);
		}catch(Exception e){
			e.printStackTrace();
		}				 
		
		fullName = person.firstName + " " + person.middleName + " " + person.lastName;
		 
		return fullName;
	}
	

	/**
	 * Generates a random letter from the alphabet
	 * 
	 * @return the generated letter (upper-case)
	 */
	protected final String generateRandomLetter() {
		return new String(new char[] { (char) ('A' + randomGenerator.nextInt(0, 25)) });
	}

	/**
	 * Append a new element with the specified name to the specified parent, for certain
	 * 
	 * @param parent
	 *            the parent
	 * @param ns
	 *            the namespace of the new element
	 * @param elementName
	 *            the name of the new element
	 * @return the new element
	 */
	protected final Element appendElement(Element parent, String ns, String elementName) {
		return appendElement(parent, ns, elementName, 1.0);
	}

	private static final Map<String, List<PersonElementWrapper>> identityMap = new HashMap<String, List<PersonElementWrapper>>();

	/**
	 * Get a random identity from the fake identity file.
	 * 
	 * @param stateParam
	 *            the state to draw the identity from, or null to draw one from any state
	 * @return the identity
	 * @throws IOException
	 */
	protected final PersonElementWrapper getRandomIdentity(String stateParam) throws IOException {
		if (identityMap.isEmpty()) {
			loadIdentityMap();
		}
		String stateParamS = stateParam;
		if (stateParamS == null) {
			List<String> states = new ArrayList<String>();
			states.addAll(identityMap.keySet());
			stateParamS = states.get(randomGenerator.nextInt(0, states.size() - 1));
		}
		List<PersonElementWrapper> identities = identityMap.get(stateParamS);
		return identities == null ? null : identities.get(randomGenerator.nextInt(0, identities.size() - 1));
	}

	private void loadIdentityMap() throws IOException {
		
		BufferedReader br = getIdentityFileReader();
		String line = null;
		//long start = System.currentTimeMillis();
		while ((line = br.readLine()) != null) {

			PersonElementWrapper person = new PersonElementWrapper(line, null);

			if (stateZipCodeData.keySet().contains(person.state)) {
				List<PersonElementWrapper> list = identityMap.get(person.state);
				if (list == null) {
					list = new ArrayList<PersonElementWrapper>();
					identityMap.put(person.state, list);
					//LOG.info("Creating identity list for state " + person.state);
				}
				list.add(person);
			}
		}
		//LOG.info("Read identity file in " + (System.currentTimeMillis() - start) + " milliseconds");
	}

	/**
	 * Load the first n records (sequentially) out of the fake identity database. Note that these are not randomly selected out of the file...they are
	 * the first records encountered. If you want a random person, use getRandomIdentity(). Use loadIdentities() if you want a fast performing method
	 * that doesn't need randomly selected records.
	 * 
	 * @param recordCount
	 *            the number of records to load
	 * @param baseDate
	 *            the date to use as the basis for relative date calculations
	 * @param stateParam
	 *            the state to filter, or null if any state
	 * @return a list of person identities
	 * @throws IOException
	 */
	protected final List<PersonElementWrapper> loadIdentities(int recordCount, DateTime baseDate, String stateParam) throws IOException {

		return loadIdentities(recordCount, 0, baseDate, stateParam);
	}

	/**
	 * Load the first n records (sequentially), starting with the record at firstRecordIndex, out of the fake identity database. Note that these are
	 * not randomly selected out of the file...they are the first records encountered. If you want a random person, use getRandomIdentity(). Use
	 * loadIdentities() if you want a fast performing method that doesn't need randomly selected records.
	 * 
	 * @param recordCount
	 *            the number of records to load
	 * @param firstRecordIndex
	 *            the index of the record to start with
	 * @param baseDate
	 *            the date to use as the basis for relative date calculations
	 * @param stateParam
	 *            the state to filter, or null if any state
	 * @return a list of person identities
	 * @throws IOException
	 */
	protected final List<PersonElementWrapper> loadIdentities(int recordCount, int firstRecordIndex, DateTime baseDate, String stateParam) throws IOException {

		BufferedReader br = getIdentityFileReader();
		String line = null;
		int lineCount = 0;
		int outputRecordCount = 0;
		List<PersonElementWrapper> people = new ArrayList<PersonElementWrapper>();

		while ((line = br.readLine()) != null && outputRecordCount < recordCount) {

			if (lineCount >= firstRecordIndex) {
				
				PersonElementWrapper person = new PersonElementWrapper(line, stateParam);

				if (stateZipCodeData.keySet().contains(person.state)) {
					people.add(person);
					outputRecordCount++;
				}
				
			}
			
			lineCount++;

		}
		if (outputRecordCount < recordCount) {
			throw new IllegalStateException("Not enough records in identity file. " + recordCount + " records requested, starting at " + firstRecordIndex + ", but only " +
					lineCount + " records in file.");
		}
		return people;
	}

	private BufferedReader getIdentityFileReader() {
		
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static-files/GeneratedIdentities.csv");
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream), 1024 * 10);
		return br;
	}

	/**
	 * Get a randomly selected city that is within the specified state, from US Postal Service data
	 * 
	 * @param state
	 *            the state in which the city exists
	 * @return the city
	 */
	protected final String getRandomCity(String state) {
		List<ZipCodeData> zipList = stateZipCodeData.get(state);
		if (zipList == null) {
			return null;
		}
		int line = randomGenerator.nextInt(0, zipList.size() - 1);
		return zipList.get(line).city;
	}
	
	
	/**
	 * Gets a random string from the input
	 * @param items
	 * 	 input items to get a random string from
	 * @return
	 * 	 random string
	 */
	protected final String randomString(String...items ){
	
		String randomString = (String)generateRandomValueFromList(items);
		
		return randomString;
	}
	
		
	protected final String randomDate() throws IOException{
		
		return randomDate("yyyy-MM-dd");
	}
	
	
	protected final String randomDate(String sdfDateFormat) throws IOException{
		
		PersonElementWrapper randomPerson = getRandomIdentity(null);
		
		String sDob = randomPerson.birthdate.toString(sdfDateFormat); 
		
		return sDob;		
	}
	
	

	/**
	 * Get a randomly selected county that is within the specified state, from US Postal Service data
	 * 
	 * @param state
	 *            the state in which the county exists
	 * @return the county
	 */
	protected final String getRandomCounty(String state) {
		List<ZipCodeData> zipList = stateZipCodeData.get(state);
		if (zipList == null) {
			return null;
		}
		int line = randomGenerator.nextInt(0, zipList.size() - 1);
		return zipList.get(line).county;
	}

	private static final Log LOG = LogFactory.getLog(AbstractSampleGenerator.class);
	protected static final DateTimeFormatter DATE_FORMATTER_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");
	protected static final DateTimeFormatter DATE_FORMATTER_MM_DD_YYYY = DateTimeFormat.forPattern("MM/dd/yyyy");

	
	public static void main(String[] args) throws Exception {

		if (args.length < 3) {
			printUsage();
			System.exit(1);
		}

		String type = args[0].toUpperCase();
		int sampleCount = Integer.parseInt(args[1]);
		String destination = args[2];

		File destinationFile = new File(destination);
		if (!destinationFile.exists()) {
			destinationFile.mkdirs();
		}

		AbstractPersonSampleGenerator generator = null;
		DateTime today = new DateTime();
		
		List<Document> criminalHistories = new ArrayList<Document>();
		
		List<Document> warrants = new ArrayList<Document>();
		
		List<Document> incidents = new ArrayList<Document>();
		
		List<Document> firearmRegistrations = new ArrayList<Document>();
		
		List<Document> juvenileHistories = new ArrayList<Document>();
		
		List<Document> custodyDocList = new ArrayList<Document>();
		
		List<Document> courtCaseDocList = new ArrayList<Document>();
		
		List<Document> vehicleCrashDocList = new ArrayList<Document>();
		
		List<Document> vehicleCrashMatthewsDocList = new ArrayList<Document>();

		if ("ALL".equals(type) || "CRIMINALHISTORY".equals(type)) {
			generator = new CriminalHistorySampleGenerator();
			criminalHistories = generator.generateSample(sampleCount, today);
		}

		if ("ALL".equals(type) || "WARRANT".equals(type)) {
			generator = new WarrantSampleGenerator();
			warrants = generator.generateSample(sampleCount, today);
		}

		if ("ALL".equals(type) || "INCIDENT".equals(type)) {
			IncidentSampleGenerator isg = new IncidentSampleGenerator();
			incidents = isg.generateSample(sampleCount, today, null);
		}

		if ("ALL".equals(type) || "FIREARM".equals(type)) {
			FirearmRegistrationSampleGenerator frsg = new FirearmRegistrationSampleGenerator();
			firearmRegistrations = frsg.generateSample(sampleCount, today, null);
		}

		if ("ALL".equals(type) || "JUVENILEHISTORY".equals(type)) {
			JuvenileHistorySampleGenerator jhsg = new JuvenileHistorySampleGenerator();
			juvenileHistories = jhsg.generateSample(sampleCount, today, null);
		}
		
		if("ALL".equals(type) || "CUSTODY".equals(type)){			
			CustodySampleGenerator custodySampleGenerator = new CustodySampleGenerator();			
			custodyDocList = custodySampleGenerator.generateCustodySamples(sampleCount);
		}
		
		if("ALL".equals(type) || "COURTCASE".equals(type)){			
			CourtCaseSampleGenerator courtCaseGenerator = new CourtCaseSampleGenerator();			
			courtCaseDocList = courtCaseGenerator.generateCourtCaseSamples(sampleCount);
		}
		
		if("ALL".equals(type) || "VEHICLECRASH".equals(type)){
			VehicleCrashSampleGenerator vehicleCrashGenerator = new VehicleCrashSampleGenerator();			
			vehicleCrashDocList = vehicleCrashGenerator.generateVehicleCrashDetailSamples(sampleCount);
		}

		if("ALL".equals(type) || "VEHICLECRASHMATTHEWS".equals(type)){
			VehicleCrashSampleGenerator vehicleCrashMatthewsGenerator = new VehicleCrashMatthewsSampleGenerator();			
			vehicleCrashMatthewsDocList = vehicleCrashMatthewsGenerator.generateVehicleCrashDetailSamples(sampleCount);
		}


		List<Document> allSamples = new ArrayList<Document>(criminalHistories.size() + warrants.size() + incidents.size() + firearmRegistrations.size() 
				+ juvenileHistories.size() + custodyDocList.size() + courtCaseDocList.size());
		
		allSamples.addAll(criminalHistories);
		allSamples.addAll(warrants);
		allSamples.addAll(incidents);
		allSamples.addAll(firearmRegistrations);
		allSamples.addAll(juvenileHistories);		
		allSamples.addAll(custodyDocList);
		allSamples.addAll(courtCaseDocList);
		allSamples.addAll(vehicleCrashDocList);
		allSamples.addAll(vehicleCrashMatthewsDocList);

		for (Document d : allSamples) {
			
			File f = File.createTempFile("sample-", ".xml", destinationFile);
			FileOutputStream fos = new FileOutputStream(f);
			XmlUtils.printNode(d, fos);
			fos.close();
		}

		LOG.info("Wrote " + allSamples.size() + " files to " + destinationFile.getAbsolutePath());		
	}

	static void printUsage() {
		
		LOG.info("Usage: java " + AbstractPersonSampleGenerator.class.getName() 
				+ " [Incident|CriminalHistory|Warrant|Firearm|JuvenileHistory|Custody|CourtCase|VehicleCrash|VEHICLECRASHMATTHEWS|All] [number of samples] [destination directory]");
	}

}
