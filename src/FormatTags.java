import enums.FieldNames;

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
            tags.add(extractTag(page, FieldNames.DEVELOPER));
            tags.add(extractTag(page, FieldNames.PUBLISHER));
            tags.add(extractTitle(page));

            System.out.println(tags);

        } catch (IOException malformedURLException) {
            System.out.println("Error with URL");
        }

    }

    private static String extractTitle(List<String> webpage) {
        String title = "";

        for(int i = 0; i < webpage.size(); i++) {
            if (webpage.get(i).contains(FieldNames.TITLE.value)) {
                title = Arrays.stream(webpage.get(i).split("[><]")).toList().get(4).substring(1);
                break;
            }
        }
        return title;
    }

    private static String extractTag(List<String> webpage, FieldNames tag) {
        String extractedTag = "";

        for(int i = 0; i < webpage.size(); i++) {
            if (webpage.get(i).contains(tag.value)) {
                extractedTag = Arrays.stream(webpage.get(i + 2).split("[><]")).toList().get(2);
                break;
            }
        }
        return extractedTag;
    }

    private static List<String> extractTags(List<String> webpage) {
        List<String> tags = new ArrayList<>();
        for(String line: webpage) {
            if (line.contains("[{\"tagid\"")) {
                tags = getTags(line);
            }
        }
        return tags;
    }

    private static List<String> getTags(String string) {
        List<String> list = Arrays.stream(string.replace("\"", "").split(",")).toList();
        List<String> tags = new ArrayList<>();

        for(String line: list) {
            if(line.contains("name")) {
                tags.add(line.split(":")[1]);
            }
        }

        return tags;
    }
}
