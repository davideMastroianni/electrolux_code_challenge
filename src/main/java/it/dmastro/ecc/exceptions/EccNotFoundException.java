package it.dmastro.ecc.exceptions;

import javax.annotation.concurrent.Immutable;
import lombok.Getter;
import lombok.Setter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@Immutable
@Getter
@Setter
public class EccNotFoundException extends AbstractThrowableProblem {

  public EccNotFoundException(String detail) {
    super(null, null, Status.NOT_FOUND, detail);
  }

  public EccNotFoundException() {
    super(null, null, Status.NOT_FOUND);
  }

}
