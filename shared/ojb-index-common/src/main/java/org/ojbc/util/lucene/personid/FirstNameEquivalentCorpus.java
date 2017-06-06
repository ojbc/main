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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.lucene.personid;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class that maintains a mapping of "diminutive" names to a canonical form.  For example, Bob and Bobby would map to Robert.  This is based upon a corpus originally downloaded
 * from https://github.com/dtrebbien/diminutives.db.
 *
 */
class FirstNameEquivalentCorpus {
	
	private static final Log log = LogFactory.getLog(FirstNameEquivalentCorpus.class);
	
	private Map<String, String> firstNameEquivalentMap;
	
	public FirstNameEquivalentCorpus() {
		
		firstNameEquivalentMap = new HashMap<String, String>();
		
		makeEquivalentGivenNameSet("Abner","Ab");
		makeEquivalentGivenNameSet("Abraham","Abe","Abey","Abie");
		makeEquivalentGivenNameSet("Ace","Acey","Acie");
		makeEquivalentGivenNameSet("Alban","Alby");
		makeEquivalentGivenNameSet("Albert","Bertie","Bert");
		makeEquivalentGivenNameSet("Alexander","Alec","Aleck","Elsie","Lex","Sander","Sawney","Xan","Zander");
		makeEquivalentGivenNameSet("Alfred","Al","Alf","Alfie","Alfy");
		makeEquivalentGivenNameSet("Alonzo","Lonnie","Lonny");
		makeEquivalentGivenNameSet("Alvin","Alvie","Alvy");
		makeEquivalentGivenNameSet("Ambrose","Ambers","Ambie","Amby","Brose");
		makeEquivalentGivenNameSet("Andrew","Andy","Drew");
		makeEquivalentGivenNameSet("Anthony","Ant","Tony");
		makeEquivalentGivenNameSet("Archibald","Archie");
		makeEquivalentGivenNameSet("Arnold","Arnie");
		makeEquivalentGivenNameSet("Arthur","Arfie","Art","Artie","Arty");
		makeEquivalentGivenNameSet("Augustus","Augie");
		makeEquivalentGivenNameSet("Barry","Baz","Bazza");
		makeEquivalentGivenNameSet("Bartholomew","Bart","Batt");
		makeEquivalentGivenNameSet("Benjamin","Ben","Benji","Benjie","Benjy","Bennie","Benny");
		makeEquivalentGivenNameSet("Bernard","Barney","Bernie");
		makeEquivalentGivenNameSet("Bradley","Brad");
		makeEquivalentGivenNameSet("Brian","Bri");
		makeEquivalentGivenNameSet("Caden","Cade");
		makeEquivalentGivenNameSet("Caleb","Cale");
		makeEquivalentGivenNameSet("Calvin","Cal");
		makeEquivalentGivenNameSet("Cameron","Cam");
		makeEquivalentGivenNameSet("Campbell","Camp");
		makeEquivalentGivenNameSet("Charles","Chad","Charley","Charlie","Chas","Chaz","Chip","Chuck","Chuckie","Chucky");
		makeEquivalentGivenNameSet("Chauncey","Chance");
		makeEquivalentGivenNameSet("Chester","Chet");
		makeEquivalentGivenNameSet("Christopher","Kit");
		makeEquivalentGivenNameSet("Clayton","Clay");
		makeEquivalentGivenNameSet("Clement","Clem","Clemmie");
		makeEquivalentGivenNameSet("Clifford","Cliff");
		makeEquivalentGivenNameSet("Cornelius","Con");
		makeEquivalentGivenNameSet("Curtis","Curt");
		makeEquivalentGivenNameSet("Cyrus","Cy");
		makeEquivalentGivenNameSet("Daniel","Dan","Danny","Dannie");
		makeEquivalentGivenNameSet("David","Dave","Davey","Davie","Davo","Davy");
		makeEquivalentGivenNameSet("Dennis","Den","Denny");
		makeEquivalentGivenNameSet("Derek","Del");
		makeEquivalentGivenNameSet("Desmond","Des");
		makeEquivalentGivenNameSet("Dominic","Dom");
		makeEquivalentGivenNameSet("Donald","Don","Donnie","Donny");
		makeEquivalentGivenNameSet("Douglas","Doug","Dougie");
		makeEquivalentGivenNameSet("Duncan","Dunky");
		makeEquivalentGivenNameSet("Ebenezer","Ebbie");
		makeEquivalentGivenNameSet("Eberhard","Ebbe");
		makeEquivalentGivenNameSet("Edward","Ed","Eddie","Eddy","Ned");
		makeEquivalentGivenNameSet("Elbert","Elbie");
		makeEquivalentGivenNameSet("Elias","Eli");
		makeEquivalentGivenNameSet("Emmanuel","Mannie","Manny");
		makeEquivalentGivenNameSet("Enrico","Rico");
		makeEquivalentGivenNameSet("Ernest","Ernie");
		makeEquivalentGivenNameSet("Eugene","Gene");
		makeEquivalentGivenNameSet("Ezekiel","Zeke");
		makeEquivalentGivenNameSet("Ferdinand","Ferd","Ferdie","Ferdy","Nandy");
		makeEquivalentGivenNameSet("Fernando","Nando");
		makeEquivalentGivenNameSet("Francis","Fran");
		makeEquivalentGivenNameSet("Franklin","Frank","Frankie");
		makeEquivalentGivenNameSet("Frederick","Freddie","Freddy");
		makeEquivalentGivenNameSet("Gabriel","Gabe");
		makeEquivalentGivenNameSet("Gareth","Gar","Gare","Gary");
		makeEquivalentGivenNameSet("Gavin","Gav");
		makeEquivalentGivenNameSet("Geoffrey","Geoff");
		makeEquivalentGivenNameSet("Gerald","Gerry","Jerry");
		makeEquivalentGivenNameSet("Gilbert","Gil");
		makeEquivalentGivenNameSet("Gordon","Gordie","Gordy");
		makeEquivalentGivenNameSet("Gregory","Greg","Gregg");
		makeEquivalentGivenNameSet("Gustav","Gus");
		makeEquivalentGivenNameSet("Harold","Hal","Harry");
		makeEquivalentGivenNameSet("Harvey","Harve");
		makeEquivalentGivenNameSet("Heathcliff","Heath");
		makeEquivalentGivenNameSet("Henry","Hank");
		makeEquivalentGivenNameSet("Herbert","Herb","Herbie");
		makeEquivalentGivenNameSet("Howard","Howie");
		makeEquivalentGivenNameSet("Hugh","Huey","Hughie");
		makeEquivalentGivenNameSet("Humphrey","Huffie");
		makeEquivalentGivenNameSet("Ignacio","Nacho","Nacio");
		makeEquivalentGivenNameSet("Indiana","Indy");
		makeEquivalentGivenNameSet("Isaac","Ike");
		makeEquivalentGivenNameSet("Isidore","Dore","Izzy");
		makeEquivalentGivenNameSet("Jacob","Jake");
		makeEquivalentGivenNameSet("James","Jamey","Jamie","Jim","Jimbo","Jimi","Jimmer","Jimmie","Jimmy");
		makeEquivalentGivenNameSet("Jason","Jace");
		makeEquivalentGivenNameSet("Jedidiah","Jed");
		makeEquivalentGivenNameSet("Jeffrey","Jeff");
		makeEquivalentGivenNameSet("Jeremy","Jez","Diarmid","Diarmi","Dermot","Diarmaid","Diarmuid");
		makeEquivalentGivenNameSet("Johannes","Joh");
		makeEquivalentGivenNameSet("John","Johnnie","Johnny","Jon","Jack");
		makeEquivalentGivenNameSet("Jonathan","Jonty");
		makeEquivalentGivenNameSet("Joseph","Jo","Joe","Joey","Pepe");
		makeEquivalentGivenNameSet("Joshua","Josh");
		makeEquivalentGivenNameSet("Kenneth","Ken","Kenny");
		makeEquivalentGivenNameSet("Kevin","Kev");
		makeEquivalentGivenNameSet("Kipling","Kip");
		makeEquivalentGivenNameSet("Kurtis","Kurt");
		makeEquivalentGivenNameSet("Lafayette","Lafe");
		makeEquivalentGivenNameSet("Lawrence","Larry","Lauren","Law","Lawrie");
		makeEquivalentGivenNameSet("Leonard","Len","Lenny");
		makeEquivalentGivenNameSet("Leopold","Leo");
		makeEquivalentGivenNameSet("Louis","Lou","Louie");
		makeEquivalentGivenNameSet("Luke","Lucky");
		makeEquivalentGivenNameSet("Martin","Marty");
		makeEquivalentGivenNameSet("Marvin","Marv");
		makeEquivalentGivenNameSet("Matthew","Mat","Matt");
		makeEquivalentGivenNameSet("Maurice","Mo","Moe","Mossie");
		makeEquivalentGivenNameSet("Max","Mac","Mack","Maxie","Maxy","Maxwell","Maximilian");
		makeEquivalentGivenNameSet("Melvin","Mel");
		makeEquivalentGivenNameSet("Mervin","Merv");
		makeEquivalentGivenNameSet("Michael","Mick","Mickey","Micky","Mike","Mikey");
		makeEquivalentGivenNameSet("Mitchell","Mitch");
		makeEquivalentGivenNameSet("Montgomery","Monty");
		makeEquivalentGivenNameSet("Mortimer","Mort");
		makeEquivalentGivenNameSet("Moshe","Moishy");
		makeEquivalentGivenNameSet("Murdoch","Murdy","Doc");
		makeEquivalentGivenNameSet("Nathan","Nate");
		makeEquivalentGivenNameSet("Newton","Newt");
		makeEquivalentGivenNameSet("Nicholas","Coll","Nic","Nick","Nicky");
		makeEquivalentGivenNameSet("Norman","Norm");
		makeEquivalentGivenNameSet("Oliver","Oli","Ollie");
		makeEquivalentGivenNameSet("Oswald","Ozzie","Ozzy");
		makeEquivalentGivenNameSet("Patrick","Paddy","Patsy");
		makeEquivalentGivenNameSet("Paul","Paulie");
		makeEquivalentGivenNameSet("Perceval","Perce","Percy");
		makeEquivalentGivenNameSet("Peregrine","Perry");
		makeEquivalentGivenNameSet("Peter","Pete","Petey");
		makeEquivalentGivenNameSet("Phillip","Phil","Pip");
		makeEquivalentGivenNameSet("Quinton","Quin","Quinn");
		makeEquivalentGivenNameSet("Ralph","Ralphie");
		makeEquivalentGivenNameSet("Randall","Randy");
		makeEquivalentGivenNameSet("Raymond","Ray");
		makeEquivalentGivenNameSet("Reginald","Reg","Reggie");
		makeEquivalentGivenNameSet("Reuben","Rube");
		makeEquivalentGivenNameSet("Richard","Dick","Dickey","Dickie","Dickon","Dicky","Hick","Rich","Richie","Rick","Rickey","Ricki","Rickie","Ricky");
		makeEquivalentGivenNameSet("Robert","Bob","Bobbi","Bobbie","Bobby","Rabbie","Rob","Robbie","Robby");
		makeEquivalentGivenNameSet("Rocco","Rocky");
		makeEquivalentGivenNameSet("Rodney","Rod");
		makeEquivalentGivenNameSet("Roger","Rog");
		makeEquivalentGivenNameSet("Ronald","Ron","Ronnie","Ronny");
		makeEquivalentGivenNameSet("Rudolph","Rudy");
		makeEquivalentGivenNameSet("Russell","Russ");
		makeEquivalentGivenNameSet("Salvador","Sal");
		makeEquivalentGivenNameSet("Scott","Scottie","Scotty");
		makeEquivalentGivenNameSet("Sebastian","Bastian","Seb");
		makeEquivalentGivenNameSet("Shlomo","Shloime");
		makeEquivalentGivenNameSet("Simon","Si","Sim");
		makeEquivalentGivenNameSet("Solomon","Sol");
		makeEquivalentGivenNameSet("Spencer","Spence");
		makeEquivalentGivenNameSet("Stanley","Stan");
		makeEquivalentGivenNameSet("Stephen","Steenie","Steve","Stevie","Steven");
		makeEquivalentGivenNameSet("Stewart","Stew","Stewie");
		makeEquivalentGivenNameSet("Stuart","Stu");
		makeEquivalentGivenNameSet("Sydney","Sid","Syd");
		makeEquivalentGivenNameSet("Sylvester","Sly");
		makeEquivalentGivenNameSet("Terence","Tel");
		makeEquivalentGivenNameSet("Thaddaeus","Tad","Thad","Thady");
		makeEquivalentGivenNameSet("Theodore","Ted","Teddie","Teddy","Theo");
		makeEquivalentGivenNameSet("Thomas","Thom","Tom","Tommy");
		makeEquivalentGivenNameSet("Timothy","Tim","Timmy");
		makeEquivalentGivenNameSet("Tobias","Toby");
		makeEquivalentGivenNameSet("Tracy","Trace");
		makeEquivalentGivenNameSet("Trevor","Trev");
		makeEquivalentGivenNameSet("Tyler","Ty");
		makeEquivalentGivenNameSet("Vance","Van");
		makeEquivalentGivenNameSet("Vernon","Vern");
		makeEquivalentGivenNameSet("Victor","Vic","Vick");
		makeEquivalentGivenNameSet("Vincent","Vin","Vince","Vinnie");
		makeEquivalentGivenNameSet("Walter","Wally","Walt");
		makeEquivalentGivenNameSet("Wesley","Wes");
		makeEquivalentGivenNameSet("William","Bill","Billy","Will","Willie","Willy");
		makeEquivalentGivenNameSet("Woodrow","Woody");
		makeEquivalentGivenNameSet("Zachary","Zach","Zack","Zak");
		makeEquivalentGivenNameSet("Zedekiah","Zed");
		
		makeEquivalentGivenNameSet("Abigail","Abbey","Abbi","Abbie","Abby","Abi","Gail","Gayle");
		makeEquivalentGivenNameSet("Adelaide","Addie");
		makeEquivalentGivenNameSet("Agnes","Aggie","Nessie");
		makeEquivalentGivenNameSet("Alexandra","Lexi","Lexie");
		makeEquivalentGivenNameSet("Alison","Ali","Allie","Ally","Aly","Allison","Allyson","Alyson");
		makeEquivalentGivenNameSet("Amanda","Mandy");
		makeEquivalentGivenNameSet("Andrea","Andi");
		makeEquivalentGivenNameSet("Angela","Ange","Angie");
		makeEquivalentGivenNameSet("Ann","Annie","Anne");
		makeEquivalentGivenNameSet("Annabelle","Belle");
		makeEquivalentGivenNameSet("Annette","Nettie");
		makeEquivalentGivenNameSet("Antonia","Toni");
		makeEquivalentGivenNameSet("Ashley","Ash","Ashless");
		makeEquivalentGivenNameSet("Audrey","Audie");
		makeEquivalentGivenNameSet("Barbara","Babs","Barb","Barbie");
		makeEquivalentGivenNameSet("Beatrice","Bea","Trixie","Beatrix");
		makeEquivalentGivenNameSet("Belle","Bell");
		makeEquivalentGivenNameSet("Bridget","Biddie","Biddy");
		makeEquivalentGivenNameSet("Candace","Candi","Candy");
		makeEquivalentGivenNameSet("Caroline","Callie","Caro","Carrie","Cary");
		makeEquivalentGivenNameSet("Cassandra","Cass","Cassie");
		makeEquivalentGivenNameSet("Catherine","Cat","Cate","Cath","Cathi","Cathie","Catie","Caty","Kate","Katie");
		makeEquivalentGivenNameSet("Cecilia","CeeCee","Ceecee","Sissy");
		makeEquivalentGivenNameSet("Cecily","Cecie");
		makeEquivalentGivenNameSet("Charlotte","Lottie");
		makeEquivalentGivenNameSet("Cherilyn","Cher");
		makeEquivalentGivenNameSet("Christina","Chrissie","Chrissy","Christy","Krissy","Kristi","Kristie","Kristy");
		makeEquivalentGivenNameSet("Constance","Connie");
		makeEquivalentGivenNameSet("Corinne","Corrie");
		makeEquivalentGivenNameSet("Cynthia","Cindy");
		makeEquivalentGivenNameSet("Danielle","Dani");
		makeEquivalentGivenNameSet("Deborah","Deb","Debi","Debbie","Debby");
		makeEquivalentGivenNameSet("Diana","Di");
		makeEquivalentGivenNameSet("Dorothy","Dodie","Doll","Dolly","Dot");
		makeEquivalentGivenNameSet("Eleanor","Ellie","Nell","Nellie","Nelly");
		makeEquivalentGivenNameSet("Elizabeth","Bess","Bessie","Bessy","Beth","Betsy","Bette","Betty","Libby","Lilibet","Liz","Lizzie","Lizzy");
		makeEquivalentGivenNameSet("Emily","Em","Emmie","Emmy");
		makeEquivalentGivenNameSet("Esther","Essie");
		makeEquivalentGivenNameSet("Ethel","Eth");
		makeEquivalentGivenNameSet("Euphemia","Effie","Phemie");
		makeEquivalentGivenNameSet("Evelyn","Evie","Lyn");
		makeEquivalentGivenNameSet("Faith","Fay","Faye");
		makeEquivalentGivenNameSet("Felicity","Fliss");
		makeEquivalentGivenNameSet("Fiona","Fi");
		makeEquivalentGivenNameSet("Florence","Flo","Florrie","Flossie","Flossy","Floy");
		makeEquivalentGivenNameSet("Frances","Fannie","Fanny","Francie","Frannie","Franny");
		makeEquivalentGivenNameSet("Gabrielle","Gabby");
		makeEquivalentGivenNameSet("Georgina","Georgie");
		makeEquivalentGivenNameSet("Gertrude","Gertie","Trudie","Trudy");
		makeEquivalentGivenNameSet("Gina","Gena");
		makeEquivalentGivenNameSet("Grace","Gracie");
		makeEquivalentGivenNameSet("Harriet","Hallie","Hally","Hattie");
		makeEquivalentGivenNameSet("Henrietta","Etta","Hettie","Hetty");
		makeEquivalentGivenNameSet("Isabella","Bella");
		makeEquivalentGivenNameSet("Jacqueline","Jackie","Jacky","Jacqui","Jacquelyn");
		makeEquivalentGivenNameSet("Janice","Jan");
		makeEquivalentGivenNameSet("Jasmine","Jas","Jaz");
		makeEquivalentGivenNameSet("Jean","Jeanie","Jeannie");
		makeEquivalentGivenNameSet("Jemima","Jem");
		makeEquivalentGivenNameSet("Jennifer","Jen","Jenn","Jenni","Jennie","Jenny");
		makeEquivalentGivenNameSet("Jessica","Jess","Jessie");
		makeEquivalentGivenNameSet("Jillian","Jilly");
		makeEquivalentGivenNameSet("Jodi","Jodie","Jody");
		makeEquivalentGivenNameSet("Judith","Judy","Judie");
		makeEquivalentGivenNameSet("Julia","Juley");
		makeEquivalentGivenNameSet("June","Junie");
		makeEquivalentGivenNameSet("Kay","Kaye");
		makeEquivalentGivenNameSet("Kerry","Kez");
		makeEquivalentGivenNameSet("Kimberley","Kimmy");
		makeEquivalentGivenNameSet("Laura","Lauri","Laurie","Lorie");
		makeEquivalentGivenNameSet("Letitia","Lettie","Letty","Tish","Tisha","Titty");
		makeEquivalentGivenNameSet("Lillian","Lil","Lillie","Lilly");
		makeEquivalentGivenNameSet("Linda","Lindie","Lindy");
		makeEquivalentGivenNameSet("Louise","Lulu");
		makeEquivalentGivenNameSet("Lydia","Liddy");
		makeEquivalentGivenNameSet("Lynn","Lynne");
		makeEquivalentGivenNameSet("Madeleine","Maddy");
		makeEquivalentGivenNameSet("Mamie","Mayme");
		makeEquivalentGivenNameSet("Margaret","Madge","Mae","Maggie","Mags","Maisie","Peg","Peggy");
		makeEquivalentGivenNameSet("Marilla","Rilla");
		makeEquivalentGivenNameSet("Matilda","Mattie","Matty","Tillie","Tilly");
		makeEquivalentGivenNameSet("Maude","Maudie");
		makeEquivalentGivenNameSet("Mavis","Mamie");
		makeEquivalentGivenNameSet("Megan","Meg","Meggie");
		makeEquivalentGivenNameSet("Melinda","Mendy","Mindy");
		makeEquivalentGivenNameSet("Melissa","Missy");
		makeEquivalentGivenNameSet("Michelle","Shell","Shelly");
		makeEquivalentGivenNameSet("Mildred","Millie","Milly");
		makeEquivalentGivenNameSet("Miranda","Randi");
		makeEquivalentGivenNameSet("Molly","Mollie","Polly");
		makeEquivalentGivenNameSet("Nancy","Nan","Nance");
		makeEquivalentGivenNameSet("Natalie","Nat");
		makeEquivalentGivenNameSet("Natasha","Tash","Tasha");
		makeEquivalentGivenNameSet("Nicole","Nicki","Nikki");
		makeEquivalentGivenNameSet("Noreen","Norrie");
		makeEquivalentGivenNameSet("Olivia","Livvy");
		makeEquivalentGivenNameSet("Pamela","Pam","Pammy");
		makeEquivalentGivenNameSet("Patricia","Patti","Patty","Tricia","Trish","Trisha");
		makeEquivalentGivenNameSet("Penelope","Penny");
		makeEquivalentGivenNameSet("Philippa","Pippa");
		makeEquivalentGivenNameSet("Priscilla","Prissy");
		makeEquivalentGivenNameSet("Prudence","Prue");
		makeEquivalentGivenNameSet("Rachel","Rae");
		makeEquivalentGivenNameSet("Rebecca","Becca","Becki","Becky","Beki");
		makeEquivalentGivenNameSet("Regina","Gina");
		makeEquivalentGivenNameSet("Rosemary","Rosie");
		makeEquivalentGivenNameSet("Roxanne","Roxy");
		makeEquivalentGivenNameSet("Saffron","Saffy");
		makeEquivalentGivenNameSet("Sandra","Sandy");
		makeEquivalentGivenNameSet("Sarah","Sadie","Sallie","Sally");
		makeEquivalentGivenNameSet("Sharon","Shaz");
		makeEquivalentGivenNameSet("Shirley","Shirl");
		makeEquivalentGivenNameSet("Stacey","Stace","Staci","Stacie");
		makeEquivalentGivenNameSet("Stephanie","Stef","Steff","Steffi","Steffie","Steffy","Steph","Stephie");
		makeEquivalentGivenNameSet("Susan","Su","Sue","Susie","Susy","Suzi","Suzie","Suzy");
		makeEquivalentGivenNameSet("Tallulah","Lula");
		makeEquivalentGivenNameSet("Tammy","Tammie");
		makeEquivalentGivenNameSet("Teresa","Terri","Terrie","Tess","Theresa");
		makeEquivalentGivenNameSet("Valerie","Val");
		makeEquivalentGivenNameSet("Victoria","Vickey","Vicki","Vickie","Vicky","Vikki");
		makeEquivalentGivenNameSet("Violet","Vi");
		makeEquivalentGivenNameSet("Virginia","Ginny");
		makeEquivalentGivenNameSet("Vivian","Viv");
		makeEquivalentGivenNameSet("Wilhelmina","Minnie");
		
	}

	private void makeEquivalentGivenNameSet(String canonicalName, String ... equivalents) {
		
		for (String equiv : equivalents) {
			if (firstNameEquivalentMap.containsKey(equiv)) {
				log.warn("Corpus already includes name " + equiv + ", mapped to " + firstNameEquivalentMap.get(equiv) + ", new suggestion is " + canonicalName);
			}
			firstNameEquivalentMap.put(equiv.toUpperCase(), canonicalName.toUpperCase());
		}
		
	}
	
	public String getEquivalentName(String name) {
		return (name != null && firstNameEquivalentMap.containsKey(name)) ? firstNameEquivalentMap.get(name) : name;
	}

}
