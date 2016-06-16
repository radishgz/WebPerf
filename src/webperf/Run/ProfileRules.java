package webperf.Run;


import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

import webperf.tools.Configure;

/**
 * Test profile with all rules
 */
public final class ProfileRules extends ProfileDefinition {

  //private static final String TEST_PROFILE_FILE 
  //= "org/sonar/plugins/web/rules/web/css-rules.xml";

  private final XMLProfileParser profileParser;

  /**
   * Creates the {@link ProfileRules}
   */
  public ProfileRules(XMLProfileParser profileParser) {
    this.profileParser = profileParser;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RulesProfile createProfile(ValidationMessages validation) {
	  String filenameOnClasspath = 
				Configure.getConfig().getCheckRule();	 
	  if (filenameOnClasspath==null || filenameOnClasspath.trim().length()==0){
		  filenameOnClasspath="CheckRules.xml";
	  }
	  RulesProfile profile = profileParser.parseResource(
			  getClass().getClassLoader(),						
			  filenameOnClasspath, validation);
	    return profile;
	  }
}