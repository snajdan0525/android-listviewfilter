package com.snalopainen.android_listview_filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final String[] ITEMS = new String[] { "East Timor", "Ecuador",
			"Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
			"Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
			"Finland", "Afghanistan", "Albania", "Algeria", "American Samoa",
			"Andorra", "Angola", "Anguilla", "Antarctica",
			"Antigua and Barbuda", "Argentina", "Armenia", "Aruba",
			"Australia", "Austria", "Azerbaijan", "Bahrain", "Bangladesh",
			"Barbados", "Belarus", "Belgium", "Monaco", "Mongolia",
			"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
			"Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
			"New Caledonia", "New Zealand", "Guyana", "Haiti",
			"Heard Island and McDonald Islands", "Honduras", "Hong Kong",
			"Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
			"Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
			"Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
			"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
			"Liechtenstein", "Lithuania", "Luxembourg", "Nicaragua", "Niger",
			"Nigeria", "Niue", "Norfolk Island", "North Korea",
			"Northern Marianas", "Norway", "Oman", "Pakistan", "Palau",
			"Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines",
			"Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
			"French Southern Territories", "Gabon", "Georgia", "Germany",
			"Ghana", "Gibraltar", "Greece", "Greenland", "Grenada",
			"Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
			"Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico",
			"Micronesia", "Moldova", "Bosnia and Herzegovina", "Botswana",
			"Bouvet Island", "Brazil", "British Indian Ocean Territory",
			"Saint Vincent and the Grenadines", "Samoa", "San Marino",
			"Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone",
			"Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
			"South Africa", "South Georgia and the South Sandwich Islands",
			"South Korea", "Spain", "Sri Lanka", "Sudan", "Suriname",
			"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
			"Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
			"The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
			"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
			"Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
			"United Arab Emirates", "United Kingdom", "United States",
			"United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
			"Vanuatu", "Vatican City", "Venezuela", "Vietnam",
			"Virgin Islands", "Wallis and Futuna", "Western Sahara",
			"British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso",
			"Burundi", "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada",
			"Cape Verde", "Cayman Islands", "Central African Republic", "Chad",
			"Chile", "China", "Reunion", "Romania", "Russia", "Rwanda",
			"Sqo Tome and Principe", "Saint Helena", "Saint Kitts and Nevis",
			"Saint Lucia", "Saint Pierre and Miquelon", "Belize", "Benin",
			"Bermuda", "Bhutan", "Bolivia", "Christmas Island",
			"Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
			"Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus",
			"Czech Republic", "Democratic Republic of the Congo", "Denmark",
			"Djibouti", "Dominica", "Dominican Republic",
			"Former Yugoslav Republic of Macedonia", "France", "French Guiana",
			"French Polynesia", "Macau", "Madagascar", "Malawi", "Malaysia",
			"Maldives", "Mali", "Malta", "Marshall Islands", "Yemen",
			"Yugoslavia", "Zambia", "Zimbabwe" };

	private ArrayList<String> mListItems;
	private ArrayList<String> mItems;
	private ArrayList<Integer> mListSectionPos;
	private PinnedListViewAdapter mAdaptor;
	private PinnedHeaderListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout header1 = (LinearLayout) inflator.inflate(
				R.layout.list_item, null);
		((TextView) header1.findViewById(R.id.textItem)).setText("HEADER 1");
		LinearLayout header2 = (LinearLayout) inflator.inflate(
				R.layout.list_item, null);
		((TextView) header2.findViewById(R.id.textItem)).setText("HEADER 2");
		LinearLayout footer = (LinearLayout) inflator.inflate(
				R.layout.list_item, null);
		((TextView) footer.findViewById(R.id.textItem)).setText("FOOTER");

		mListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);

		IndexScrollBarView indexBarView = (IndexScrollBarView) inflator
				.inflate(R.layout.index_bar_view, mListView, false);

		mListView.setIndexScrollBarView(indexBarView);

		mListItems = new ArrayList<String>(Arrays.asList(ITEMS));
		mItems = new ArrayList<String>();
		mListSectionPos = new ArrayList<Integer>();
		getSectionPosFromWordsList(mListItems);
		mAdaptor = new PinnedListViewAdapter(this, mItems, mListSectionPos);
		indexBarView.setIndexScrollViewData(mListView, mItems,
				mListSectionPos);
		mListView.setAdapter(mAdaptor);
	}

	private void sortArray(ArrayList<String> array) {
		Collections.sort(array, new SortWordsIgnoreCase());
	}

	private void getSectionPosFromWordsList(ArrayList<String> datas) {
		sortArray(datas);
		String prev_section = "";
		for (String current_word : datas) {
			String current_sctionPos = current_word.substring(0, 1)
					.toUpperCase(Locale.getDefault());
			if (!prev_section.equals(current_sctionPos)) {
				mItems.add(current_sctionPos);
				mItems.add(current_word);
				mListSectionPos.add(mItems.indexOf(current_sctionPos));
				prev_section = current_sctionPos;
			} else {
				mItems.add(current_word);
			}
		}
	}

	public class SortWordsIgnoreCase implements Comparator<String> {
		public int compare(String first, String second) {
			return first.compareToIgnoreCase(second);
		}
	}
}
