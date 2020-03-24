package sqlTransfer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchUtility {

	//This function takes the search criteria and all photo data and finds matches between the two
	//Must Pass searchDist and searchLoc as doubles, everything else is a string

    public static ArrayList<String[]> searchFunc(String minDate, String maxDate, String captionSearch,
    double searchDist, double[] searchLoc, ArrayList<String[]> photoDetails) {

        ArrayList<String[]> photoGallery = new ArrayList<String[]>();

        for (int i = 0; i < photoDetails.size(); i++) {

			//data: 0 image name. 1 caption, 2 date, 3 lat, 4 lon
            String[] data = photoDetails.get(i);

            //Get date
            Date date = new Date();
			Date minDateD = new Date();
			Date maxDateD = new Date();
            String[] contents = data[2].split("_");
            try {
                date = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(contents[0] + "_" + contents[1]);
				minDateD = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(minDate);
				maxDateD = new SimpleDateFormat("yyyyMMdd_HHmmss").parse(maxDate);
            } catch (ParseException e) {}

            //Get Distance
            double[] loc = new double[2];
            loc[0] = Double.parseDouble(data[3]);
            loc[1] = Double.parseDouble(data[4]);
            double dist = getDist(loc, searchLoc);

            if (date.compareTo(minDateD) >= 0 && date.compareTo(maxDateD) <= 0 && dist <= searchDist) {
                if (captionSearch == null) {
                    photoGallery.add(data);
                } else if (data[2].matches("(.*)" + captionSearch + "(.*)")) {
                    photoGallery.add(data);
                }
            }
        }

        return photoGallery;
    }

    public static double getDist(double[] loc1, double[] loc2){
        // Radius of Earth in km
        int R = 6371;
        // Get lat/long of points, convert to radians
        double lat1 = loc1[0] * Math.PI/180;
        double lat2 = loc2[0] * Math.PI/180;
        double long1 = loc1[1] * Math.PI/180;
        double long2 = loc2[1] * Math.PI/180;
        // Calculate angular difference
        double dLong= long1 - long2;
        double dLat = lat1 - lat2;

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(lat1) * Math.cos(lat2) *
                Math.sin(dLong/2) * Math.sin(dLong/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
}
