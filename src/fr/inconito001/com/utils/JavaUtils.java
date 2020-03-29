package fr.inconito001.com.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

public final class JavaUtils {
	private static final CharMatcher CHAR_MATCHER_ASCII;
	private static final Pattern UUID_PATTERN;

	static {
		CHAR_MATCHER_ASCII = CharMatcher.inRange('0', '9').or(CharMatcher.inRange('a', 'z')).or(CharMatcher.inRange('A', 'Z')).or(CharMatcher.WHITESPACE).precomputed();
		UUID_PATTERN = Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}");
	}

	public static boolean isUUID(final String string) {
		return JavaUtils.UUID_PATTERN.matcher(string).find();
	}

	public static boolean isAlphanumeric(final String string) {
		return JavaUtils.CHAR_MATCHER_ASCII.matchesAllOf((CharSequence) string);
	}

	public static String format(final Number number) {
		return format(number, 5);
	}

	public static String format(final Number number, final int decimalPlaces) {
		return format(number, decimalPlaces, RoundingMode.HALF_DOWN);
	}

	public static String format(final Number number, final int decimalPlaces, final RoundingMode roundingMode) {
		Preconditions.checkNotNull((Object) number, (Object) "The number cannot be null");
		return new BigDecimal(number.toString()).setScale(decimalPlaces, roundingMode).stripTrailingZeros()
				.toPlainString();
	}

	public static String andJoin(final Collection<String> collection, final boolean delimiterBeforeAnd) {
		return andJoin(collection, delimiterBeforeAnd, ", ");
	}

	@SuppressWarnings("unused")
	public static String getLeftTime(final Date end) {
		final Date d1 = new Date(System.currentTimeMillis());
		final Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
		cal.setTime(d1);
		final long t1 = cal.getTimeInMillis();
		cal.setTime(end);
		long diff = Math.abs(cal.getTimeInMillis() - t1);
		final int ONE_DAY = 86400000;
		final int ONE_HOUR = 3600000;
		final int ONE_MINUTE = 60000;
		final int ONE_SECOND = 1000;
		final long d2 = diff / 86400000L;
		diff %= 86400000L;
		final long h = diff / 3600000L;
		diff %= 3600000L;
		final long m = diff / 60000L;
		diff %= 60000L;
		final long s = diff / 1000L;
		if (d2 > 0L) {
			return String.valueOf(d2) + " jour" + addS(d2) + ", " + h + " heure" + addS(h) + " et " + m + " minute"
					+ addS(m);
		}
		if (h > 0L) {
			return String.valueOf(h) + " heure" + addS(h) + " et " + m + " minute" + addS(m);
		}
		return String.valueOf(m) + " minute" + addS(m) + " et " + s + " seconde" + addS(s);
	}

	private static String addS(final long t) {
		return (t > 0L) ? "s" : "";
	}

	public static String andJoin(final Collection<String> collection, final boolean delimiterBeforeAnd,
			final String delimiter) {
		if (collection == null || collection.isEmpty()) {
			return "";
		}
		final List<String> contents = new ArrayList<String>(collection);
		final String last = contents.remove(contents.size() - 1);
		@SuppressWarnings("rawtypes")
		final StringBuilder builder = new StringBuilder(Joiner.on(delimiter).join((Iterable) contents));
		if (delimiterBeforeAnd) {
			builder.append(delimiter);
		}
		return " et " + last;
	}

	public static long parse(final String input) {
		if (input == null || input.isEmpty()) {
			return -1L;
		}
		long result = 0L;
		StringBuilder number = new StringBuilder();
		for (int i = 0; i < input.length(); ++i) {
			final char c = input.charAt(i);
			if (Character.isDigit(c)) {
				number.append(c);
			} else {
				final String str;
				if (Character.isLetter(c) && !(str = number.toString()).isEmpty()) {
					result += convert(Integer.parseInt(str), c);
					number = new StringBuilder();
				}
			}
		}
		return result;
	}

	private static long convert(final int value, final char unit) {
		switch (unit) {
		case 'y': {
			return value * TimeUnit.DAYS.toMillis(365L);
		}
		case 'M': {
			return value * TimeUnit.DAYS.toMillis(30L);
		}
		case 'd': {
			return value * TimeUnit.DAYS.toMillis(1L);
		}
		case 'h': {
			return value * TimeUnit.HOURS.toMillis(1L);
		}
		case 'm': {
			return value * TimeUnit.MINUTES.toMillis(1L);
		}
		case 's': {
			return value * TimeUnit.SECONDS.toMillis(1L);
		}
		default: {
			return -1L;
		}
		}
	}

	public static boolean isInteger(final String string) {
		boolean isInteger = true;
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException ex) {
			isInteger = false;
		}
		return isInteger;
	}

	public static boolean isDouble(final String string) {
		boolean isDouble = true;
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException ex) {
			isDouble = false;
		}
		return isDouble;
	}

	public static int randomInt(final int min, final int max) {
		final Random rn = new Random();
		final int range = max - min + 1;
		final int randomNum = rn.nextInt(range) + min;
		return randomNum;
	}

	public static Integer tryParseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static Double tryParseDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public static <E> List<E> createList(Object object, Class<E> type) {
		List<E> output = new ArrayList<>();
		if ((object != null) && ((object instanceof List))) {
			List<?> input = (List<?>) object;
			for (Object value : input) {
				if (value != null) {
					if (value.getClass() != null) {
						if (!type.isAssignableFrom(value.getClass())) {
							String simpleName = type.getSimpleName();
							throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
						}
						E e = type.cast(value);
						output.add(e);
					}
				}
			}
		}
		return output;
	}
	
	@SuppressWarnings("rawtypes")
	public static <E> Set<E> castSet(Object object, Class<E> type){
		HashSet<E> output = new HashSet<E>();
		if(object != null && object instanceof List){
			List input = (List) object;
			for(Object value : input){
				if(value == null || value.getClass() == null) continue;
				if(!type.isAssignableFrom(value.getClass())){
					String simpleName = type.getSimpleName();
					throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
				}
				E e = type.cast(value);
				output.add(e);
			}
		}
		return output;
	}

	@SuppressWarnings("rawtypes")
	public static <K, V> Map<K, V> castMap(Object object, Class<K> keyClass, Class<V> valueClass){
		HashMap<K, V> output = new HashMap<K, V>();
		if(object != null && object instanceof Map){
			Map input = (Map) object;
			String keyClassName = keyClass.getSimpleName();
			String valueClassName = valueClass.getSimpleName();
			for(Object key : input.keySet().toArray()){
				if(key != null && !keyClass.isAssignableFrom(key.getClass())){
					throw new AssertionError("Cannot cast to HashMap: " + keyClassName + ", " + keyClassName + ". Value " + valueClassName + " is not a " + keyClassName);
				}
				Object value = input.get(key);
				if(value != null && !valueClass.isAssignableFrom(value.getClass())){
					throw new AssertionError("Cannot cast to HashMap: " + valueClassName + ", " + valueClassName + ". Key " + key + " is not a " + valueClassName);
				}
				output.put(keyClass.cast(key), valueClass.cast(value));
			}
		}
		return output;
	}
}