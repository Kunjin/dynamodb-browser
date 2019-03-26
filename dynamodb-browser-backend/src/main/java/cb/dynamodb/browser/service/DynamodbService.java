package cb.dynamodb.browser.service;

import com.amazonaws.auth.profile.ProfilesConfigFile;
import com.amazonaws.auth.profile.internal.BasicProfile;
import com.amazonaws.regions.Regions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DynamodbService {

    public List<String> getRegions() {
        List<String> regionList = new ArrayList<>();
         for (Regions region : Regions.values()) {
             regionList.add(region.getName());
         }
        return regionList;
    }

    public Set<String> getProfilesInCredentials() {
        Map<String, BasicProfile> profilesMap = new ProfilesConfigFile().getAllBasicProfiles();
        return profilesMap.keySet();
    }
}
