
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.SegmentRepository;
import domain.Segment;

@Component
@Transactional
public class StringToSegmentConverter implements Converter<String, Segment> {

	@Autowired
	private SegmentRepository	segmentRepository;


	@Override
	public Segment convert(final String text) {
		Segment result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.segmentRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
