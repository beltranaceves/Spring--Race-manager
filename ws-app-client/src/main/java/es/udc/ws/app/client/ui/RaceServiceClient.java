package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientRaceService;
import es.udc.ws.app.client.service.ClientRaceServiceFactory;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
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
                int dorsalNumber = clientRaceService.collectInscription(Long.parseLong(args[1]), args[2]);

                System.out.println("Dorsal number: " + dorsalNumber + " collected successfully by racer with " +
                        " credit card number: " + args[2]);

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
                "    [deliverNumber] RaceServiceClient -c <inscriptionId> <creditCardNumber>");
    }

}

