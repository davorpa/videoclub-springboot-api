package es.seresco.cursojee.videoclub.view.dto.actor;

import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.seresco.cursojee.videoclub.view.dto.Identificable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class ResponseActorDTO extends ActorDTO implements Identificable<Long>
{

	private static final long serialVersionUID = 6955163289795453315L;

	@JsonProperty(value = "id", index = 0)
	@Positive
	@EqualsAndHashCode.Include
	private Long id;

}
