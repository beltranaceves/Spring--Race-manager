namespace java es.udc.ws.app.thrift

struct ThriftInscriptionDto {
    1: i64 inscriptionId;
    2: i64 raceId;
    3: i32 dorsalNumber;
    4: bool collected;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftAlreadyInscribedException {
    1: i64 raceId;
    2: string userEmail;
}

exception ThriftInscriptionDateOverException {
    1: i64 raceId;
    2: string dateOver;
}

exception ThriftMaxParticipantsException {
    1: i64 raceId;
    2: i32 maxParticipants;
}

service ThriftRaceService {

   list<ThriftInscriptionDto> findInscriptionByUserEmail(1: string userEmail) throws (1: ThriftInputValidationException e)

   i64 inscribeRace(1: i64 raceId, 2: string userEmail, 3: string creditCardNumber) throws (1: ThriftInputValidationException e,
   2: ThriftInstanceNotFoundException ee, 3: ThriftAlreadyInscribedException eee, 4: ThriftInscriptionDateOverException eeee,
   5: ThriftMaxParticipantsException eeeee)

}