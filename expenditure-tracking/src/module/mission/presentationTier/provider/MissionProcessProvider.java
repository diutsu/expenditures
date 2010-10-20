package module.mission.presentationTier.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import module.mission.domain.MissionProcess;
import module.mission.domain.MissionSystem;
import myorg.presentationTier.renderers.autoCompleteProvider.AutoCompleteProvider;

import org.apache.commons.lang.StringUtils;

public class MissionProcessProvider implements AutoCompleteProvider {

    @Override
    public Collection getSearchResults(Map<String, String> argsMap, String value, int maxCount) {
	final String currentValue = StringUtils.trim(value);

	final List<MissionProcess> result = new ArrayList<MissionProcess>();
	final MissionSystem missionSystem = MissionSystem.getInstance();
	for (final MissionProcess missionProcess : missionSystem.getMissionProcessesSet()) {
	    if (missionProcess.getProcessIdentification().equals(currentValue) || missionProcess.getProcessNumber().equals(currentValue)) {
		result.add(missionProcess);
	    }
	}
	return result;
    }

}
