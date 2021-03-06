package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.ClientRaceServiceFactory;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.dto.ClientRaceDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class RaceServiceClient {

    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }

        ClientRaceService clientRaceService =
                ClientRaceServiceFactory.getService();

        if ("-reg".equalsIgnoreCase(args[0])) {

            validateArgs(args, 4, new int[] {1});

            // [register] RaceServiceClient -reg <raceId> <userEmail> <creditCardNumber>

            try {
                Long inscriptionId = clientRaceService.inscribeRace(Long.parseLong(args[1]), args[2], args[3]);

                System.out.println("Registered successfully with inscription code " + inscriptionId +
                                    " in race " + args[1]);

            } catch (NumberFormatException | InstanceNotFoundException | InputValidationException |
                        ClientAlreadyInscribedException | ClientInscriptionDateOverException |
                        ClientMaxParticipantsException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-f".equalsIgnoreCase(args[0])) {

            validateArgs(args, 2, new int[] {});

            // [find]     RaceServiceClient -f <userEmail>

            try {
                List<ClientInscriptionDto> inscriptions = clientRaceService.findInscriptionByUserEmail(args[1]);
                System.out.println("Found " + inscriptions.size() +
                        " inscription(s) with email '" + args[1] + "'");
                for (int i = 0; i < inscriptions.size(); i++) {
                    ClientInscriptionDto inscriptionDto = inscriptions.get(i);
                    System.out.println("Inscription Id: " + inscriptionDto.getInscriptionId() +
                            ", Race Id: " + inscriptionDto.getRaceId() +
                            ", Dorsal Number: " + inscriptionDto.getDorsalNumber() +
                            ", Collected: " + inscriptionDto.getCollected());
                }
            } catch (InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-d".equalsIgnoreCase(args[0])) {

            validateArgs(args, 3, new int[] {1});

            // [deliverNumber]      RaceServiceClient -d <inscriptionId> <creditCardNumber>

            try{
                clientRaceService.collectInscription(Long.parseLong(args[1]), args[2]);

                System.out.println("Dorsal collected");

            } catch (InputValidationException | ClientDorsalAlreadyCollectedException |
                    ClientCreditCardDoesNotMatchException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-fr".equalsIgnoreCase(args[0])){

            validateArgs(args, 2, new int[] {});

            // [findRace] RaceServiceClient -fr <raceId>

            try {
                int numberOfInscribed = clientRaceService.findRace(Long.parseLong(args[1]));

                System.out.println("Plazas libres = " + numberOfInscribed);

            } catch (InputValidationException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-ar".equalsIgnoreCase(args[0])){

            validateArgs(args, 6, new int[] {4, 5});

            // [addRace] RaceServiceClient -ar <city> <description> <date> <inscriptionPrice> <maxParticipants>
            
            try {
                ClientRaceDto raceDto = new ClientRaceDto(args[1],
                        args[2], LocalDateTime.parse(args[3]),
                        Double.parseDouble(args[4]), Integer.parseInt(args[5]));
                Long raceId = clientRaceService.addRace(raceDto
                		);

                System.out.println("Carrera " + raceId + " añadida correctamente");

            } catch (NumberFormatException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        } else if ("-frs".equalsIgnoreCase(args[0])){
            if (args.length == 2) {
                validateArgs(args, 2, new int[] {});

                // [findRace] RaceServiceClient -fr <raceId>

                try {
                    List<ClientRaceDto> clientRaceDtos = clientRaceService.findRacesByDateAndCity(args[1], null);

                    for(int i = 0; i < clientRaceDtos.size(); i++) {
                        System.out.println(clientRaceDtos.get(i).getRaceId());
                    }

                } catch (InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            } else {
                validateArgs(args, 3, new int[] {});

                // [findRace] RaceServiceClient -fr <raceId>

                try {
                    List<ClientRaceDto> clientRaceDtos = clientRaceService.findRacesByDateAndCity(
                            args[1], args[2]);

                    for(int i = 0; i < clientRaceDtos.size(); i++) {
                        System.out.println("id: " + clientRaceDtos.get(i).getRaceId()
                        + " ciudad: " + clientRaceDtos.get(i).getCity()
                        + " descripción: " + clientRaceDtos.get(i).getDescription()
                        + " fecha/hora: " + clientRaceDtos.get(i).getDate().toString()
                        + " parcitipantes máximos: " + clientRaceDtos.get(i).getMaxParticipants()
                        + " participantes inscritos: " + clientRaceDtos.get(i).getNumberOfInscribed());
                    }

                } catch (InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }

            }
        }

    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [register]      RaceServiceClient -reg <raceId> <userEmail> <creditCardNumber>\n" +
                "    [find]          RaceServiceClient -f <userEmail>\n" +
                "    [findRace]      RaceServiceClient -fr <raceId>\n" +
                "    [findRaces]     RaceServiceClient -frs <date> <city>\n" +
                "    [deliverNumber] RaceServiceClient -d <inscriptionId> <creditCardNumber>\n" +
                "    [addRace]       RaceServiceClient -ar <city, description, date, inscriptionPrice, maxParticipants>");
    }

}

