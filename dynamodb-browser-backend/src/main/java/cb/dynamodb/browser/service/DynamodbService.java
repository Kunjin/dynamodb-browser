package cb.dynamodb.browser.service;

import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.regions.Regions;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class DynamodbService {

    public Regions[] getRegions() {
        return Regions.values();
    }

    public Set<String> getProfilesInCredentials() {
        Map<String, BasicProfile> profilesMap = new ProfilesConfigFile().getAllBasicProfiles();
        return profilesMap.keySet();
    }
}
