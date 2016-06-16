package webperf.Run;

import java.util.Collection;
import java.util.List;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.plugins.web.rules.WebRulesRepository;

public class WebRuleFinder implements RuleFinder {

    private final WebRulesRepository repository;
    private final List<Rule> rules;

    public WebRuleFinder() {
      repository = new WebRulesRepository(new AnnotationRuleParser());
      rules = repository.createRules();
    }

     public Rule find(RuleQuery query) {
      return null;
    }

     public Collection<Rule> findAll(RuleQuery query) {
      return null;
    }

     
    public Rule findByKey(String repositoryKey, String key) {
      for (Rule rule : rules) {
        if (rule.getKey().equals(key)) {
          return rule;
        }
      }
      return null;
    }

     public Rule findById(int ruleId) {
      return null;
    }

	 
	public Rule findByKey(RuleKey arg0) {
		// TODO Auto-generated method stub
		return null;
	}
  }