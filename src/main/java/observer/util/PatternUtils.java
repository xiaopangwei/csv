package observer.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Description: </p>
 * <p>Company: Harbin Institute of Technology</p>
 *
 * @author weihuang
 * @date 2018/10/11
 * @time 8:29 PM
 */
public class PatternUtils {
    private static final String  CSV_SOURCE_NAME_REGULAR_EXP = "^([a-z]+)[_]([a-z]+)(\\d{1,3})[_](\\d{1,3})(.csv)$";
    private static final Pattern CSV_SOURCE_NAME_PATTERNP    = Pattern.compile(CSV_SOURCE_NAME_REGULAR_EXP);

    private static final int INDEX_OF_SOURCE_TYPE         = 1;
    private static final int INDEX_OF_MIDDLE_FILE_NAME    = 2;
    private static final int INDEX_OF_DXS_PORT            = 3;
    private static final int INDEX_OF_PROCESS_ID_PER_PORT = 4;
    private static final int INDEX_OF_EXTENSION_NAME      = 5;

    public static String getSourceType(String sourceName) {
        Matcher m = CSV_SOURCE_NAME_PATTERNP.matcher(sourceName);
        if (m.find()) {
            return m.group(INDEX_OF_SOURCE_TYPE);
        }
        return null;
    }

    public static int getDxsPort(String sourceName) {
        Matcher m = CSV_SOURCE_NAME_PATTERNP.matcher(sourceName);
        if (m.find()) {
            return Integer.parseInt(m.group(INDEX_OF_DXS_PORT));
        }
        return 0;
    }

    public static int getProcessId(String sourceName) {
        Matcher m = CSV_SOURCE_NAME_PATTERNP.matcher(sourceName);
        if (m.find()) {
            return Integer.parseInt(m.group(INDEX_OF_PROCESS_ID_PER_PORT));
        } else {
            return 0;
        }
    }

    public static boolean isValidSource(String sourceName) {
        Matcher m = CSV_SOURCE_NAME_PATTERNP.matcher(sourceName);
        return m.find();
    }

}
