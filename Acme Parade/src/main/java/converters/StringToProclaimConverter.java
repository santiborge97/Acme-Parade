
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ProclaimRepository;
import domain.Proclaim;

@Component
@Transactional
public class StringToProclaimConverter implements Converter<String, Proclaim> {

	@Autowired
	private ProclaimRepository	proclaimRepository;


	@Override
	public Proclaim convert(final String text) {
		Proclaim result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.proclaimRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
