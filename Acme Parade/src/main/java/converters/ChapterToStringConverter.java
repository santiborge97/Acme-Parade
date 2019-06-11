
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Chapter;

@Component
@Transactional
public class ChapterToStringConverter implements Converter<Chapter, String> {

	@Override
	public String convert(final Chapter chapter) {
		String result;

		if (chapter == null)
			result = null;
		else
			result = String.valueOf(chapter.getId());

		return result;
	}
}
