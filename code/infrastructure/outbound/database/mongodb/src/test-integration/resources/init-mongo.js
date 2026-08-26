db = db.getSiblingDB('mydatabase');

db.outbox.insertOne({
    _id: UUID("ec9633ff-1137-4f3a-87ef-ea7738ad411f"),
    outboxId: UUID("ec9633ff-1137-4f3a-87ef-ea7738ad411f"),
    aggregateType: "USER",
    aggregateId: "123",
    messageHeader: {
        messageId: "4059510b-ceb3-4d4c-913e-1759acbd63b1",
        messageDate: ISODate("2026-01-01T00:00:00Z")
    },
    action: "USER_CREATED",
    payload: "{\"id\":\"123\"}",
    status: "PENDING",
    retryCount: 0,
    createdAt: ISODate("2026-01-01T00:00:00Z"),
    processedAt: null
});