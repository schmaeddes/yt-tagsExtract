import enums.Properties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormatTags {

    public static void main(String[] args) {

        String steamId = "870780";

        try {
            URL steamUrl = new URL("https://store.steampowered.com/app/" + steamId);
            BufferedReader in = new BufferedReader(new InputStreamReader(steamUrl.openStream()));
            List<String> page = new ArrayList<>();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                page.add(inputLine);
            }
            in.close();

            List<String> tags = extractTags(page);
            tags.add(extractProperty(page, Properties.DEVELOPER));
            tags.add(extractProperty(page, Properties.PUBLISHER));
            tags.add(extractProperty(page, Properties.TITLE));

            System.out.println(tags);

        } catch (IOException malformedURLException) {
            System.out.println("Error with URL");
        }

    }

    private static String extractProperty(List<String> webpage, Properties tag) {
        String extractedTag = "";

        for(int i = 0; i < webpage.size(); i++) {
            if (webpage.get(i).contains(tag.value)) {

                if (tag.equals(Properties.TITLE)) {
                    extractedTag = Arrays.stream(webpage.get(i).split("[><]")).toList().get(4).substring(1);
                } else {
                    extractedTag = Arrays.stream(webpage.get(i + 2).split("[><]")).toList().get(2);
                }
                break;
            }
        }
        return extractedTag;
    }

    private static List<String> extractTags(List<String> webpage) {
        List<String> tags = new ArrayList<>();
        for(String line: webpage) {
            if (line.contains("[{\"tagid\"")) {
                List<String> list = Arrays.stream(line.replace("\"", "").split(",")).toList();

                for(String entry: list) {
                    if(entry.contains("name")) {
                        tags.add(entry.split(":")[1]);
                    }
                }
            }
        }
        return tags;
    }

}
