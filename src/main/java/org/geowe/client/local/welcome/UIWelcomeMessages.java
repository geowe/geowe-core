package org.geowe.client.local.welcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface UIWelcomeMessages extends Messages {
	UIWelcomeMessages INSTANCE = GWT.create(UIWelcomeMessages.class);

	String news();

	String text1();

	String text2();

	String helpUs();

	String disclaimer();

	String privacy();

	String TermsOfUse();

	String involved();

	String license();

}
