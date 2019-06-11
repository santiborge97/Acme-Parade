
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ChapterRepository;
import domain.Chapter;

@Component
@Transactional
public class StringToChapterConverter implements Converter<String, Chapter> {

	@Autowired
	private ChapterRepository	chapterRepository;


	@Override
	public Chapter convert(final String text) {
		Chapter result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.chapterRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
