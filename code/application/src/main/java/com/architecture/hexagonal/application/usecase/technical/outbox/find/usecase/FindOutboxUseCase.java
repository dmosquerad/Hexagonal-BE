package com.architecture.hexagonal.application.usecase.technical.outbox.find.usecase;

import com.architecture.hexagonal.application.usecase.technical.outbox.find.input.FindOutboxInput;
import com.architecture.hexagonal.domain.model.entity.outbox.Outbox;
import java.util.List;
import lombok.NonNull;

public interface FindOutboxUseCase {

  List<Outbox> execute(@NonNull FindOutboxInput findOutboxInput);
}
