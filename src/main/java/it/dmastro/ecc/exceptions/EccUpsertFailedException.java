package it.dmastro.ecc.exceptions;

import javax.annotation.concurrent.Immutable;
import lombok.Getter;
import lombok.Setter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Immutable
@Getter
@Setter
public class EccUpsertFailedException extends AbstractThrowableProblem {

  public EccUpsertFailedException(String detail) {
    super(null, null, Status.UNPROCESSABLE_ENTITY, detail);
  }

  public EccUpsertFailedException() {
    super(null, null, Status.UNPROCESSABLE_ENTITY);
  }

}
