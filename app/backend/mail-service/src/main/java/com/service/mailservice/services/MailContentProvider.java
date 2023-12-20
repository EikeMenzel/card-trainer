package com.service.mailservice.services;

public class MailContentProvider {
    public static String dailyLearnReminder = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Tägliche Lern Erinnerung</title>
              <style>
                body {
                  font-family: Arial, sans-serif;
                  margin: 0;
                  padding: 0;
                  background-color: #f0f0f0;
                }
                        
                .header {
                  background-color: #4CAF50;
                  color: #fff;
                  text-align: center;
                  padding: 10px 0;
                }
                        
                .content {
                  text-align: center;
                  margin-top: 50px;
                }
                        
                .button {
                  background-color: #4CAF50;
                  color: #fff;
                  border: none;
                  padding: 10px 20px;
                  border-radius: 20px;
                  cursor: pointer;
                }
                        
                .footer {
                  text-align: center;
                  padding: 10px 0;
                  background-color: #4CAF50;
                  color: #fff;
                  position: absolute;
                  bottom: 0;
                  width: 100%;
                }
              </style>
            </head>
            <body>
            <div class="header">
              <h1>Tägliche Lern Erinnerung</h1>
            </div>
                        
            <div class="content">
              <p>Hallo, ${username}</p>
              <p>Wir möchten dich daran erinnern, täglich zu lernen und deine Fähigkeiten zu verbessern.</p>
              <p>Klicke auf den unten stehenden Button, um mit deiner Lernsession zu beginnen.</p>
              <a href="${dailyLearnUrl}">
                <button class="button">Jetzt lernen</button>
              </a>
              <p>Wenn du diese E-Mail nicht erwartet hast, kannst du sie einfach ignorieren.</p>
            </div>
                        
            <div class="footer">
              <p>SWTP-Projekt von Tim Martin Süllner, Eike Torben Menzel, Maria Kazim und Gürkan Deniz</p>
            </div>
            </body>
            </html>
            """;

    public static String mailPasswordReset = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Password Reset</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f0f0f0;
                    }
                        
                    .header {
                        background-color: #4CAF50;
                        color: #fff;
                        text-align: center;
                        padding: 10px 0;
                    }
                        
                    .content {
                        text-align: center;
                        margin-top: 50px;
                    }
                        
                    .button {
                        background-color: #4CAF50;
                        color: #fff;
                        border: none;
                        padding: 10px 20px;
                        border-radius: 20px;
                        cursor: pointer;
                    }
                        
                    .footer {
                        text-align: center;
                        padding: 10px 0;
                        background-color: #4CAF50;
                        color: #fff;
                        position: absolute;
                        bottom: 0;
                        width: 100%;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>Passwort Zurücksetzen</h1>
                </div>
                        
                <div class="content">
                    <p>Sie haben diese E-Mail erhalten, weil Sie einen Password-Reset angefragt haben.</p>
                    <p>Drücken Sie auf 'Passwort Zurücksetzen', um ein neues Passwort zu vergeben.</p>
                    <a href="${resetUrl}">
                        <button class="button">Passwort Zurücksetzen</button>
                    </a>
                    <p>Sollten Sie keinen Passwort-Reset bei uns angefragt haben, ignorieren Sie diese E-Mail.</p>
                    <p>Jedoch sollten Sie in diesen Fall zur Sicherheit auf unserer Internetseite einen Password-Reset anfragen.</p>
                </div>
                        
                <div class="footer">
                    <p>SWTP-Projekt von Tim Martin Süllner, Eike Torben Menzel, Maria Kazim und Gürkan Deniz</p>
                </div>
            </body>
            </html>
            """;
    public static String MailVerification = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Verification</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f0f0f0;
                    }
                        
                    .header {
                        background-color: #4CAF50;
                        color: #fff;
                        text-align: center;
                        padding: 10px 0;
                    }
                        
                    .content {
                        text-align: center;
                        margin-top: 50px;
                    }
                        
                    .button {
                        background-color: #4CAF50;
                        color: #fff;
                        border: none;
                        padding: 10px 20px;
                        border-radius: 20px;
                        cursor: pointer;
                    }
                        
                    .footer {
                        text-align: center;
                        padding: 10px 0;
                        background-color: #4CAF50;
                        color: #fff;
                        position: absolute;
                        bottom: 0;
                        width: 100%;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>SWTP E-Mail Bestätigen</h1>
                </div>
                        
                <div class="content">
                    <p>Sie haben diese E-Mail erhalten, um Ihre Identität zu bestätigen.</p>
                    <a href="${verificationUrl}">
                        <button class="button">Bestätigen</button>
                    </a>
                    <p>Sollten sie keinen Account bei uns erstellt haben ignorieren sie diese E-Mail.</p>
                </div>
                        
                <div class="footer">
                    <p>SWTP-Projekt von Tim Martin Süllner, Eike Torben Menzel, Maria Kazim und Gürkan Deniz</p>
                </div>
            </body>
            </html>
            """;

    public static String shareDeck = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Verification</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 0;
                        background-color: #f0f0f0;
                    }
                        
                    .header {
                        background-color: #4CAF50;
                        color: #fff;
                        text-align: center;
                        padding: 10px 0;
                    }
                        
                    .content {
                        text-align: center;
                        margin-top: 50px;
                    }
                        
                    .button {
                        background-color: #4CAF50;
                        color: #fff;
                        border: none;
                        padding: 10px 20px;
                        border-radius: 20px;
                        cursor: pointer;
                    }
                        
                    .footer {
                        text-align: center;
                        padding: 10px 0;
                        background-color: #4CAF50;
                        color: #fff;
                        position: absolute;
                        bottom: 0;
                        width: 100%;
                    }
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>SWTP Deck Teilen</h1>
                </div>
                        
                <div class="content">
                    <p>${senderName} hat ein Deck mit dem Name '${deckName}' mit Ihnen geteilt.</p>
                    <a href="${shareDeckUrl}">
                        <button class="button">Deck integrieren</button>
                    </a>
                    <p>Falls Sie keine Decks erwarten, ignorieren Sie bitte diese E-Mail.</p>
                </div>
                        
                <div class="footer">
                    <p>SWTP-Projekt von Tim Martin Süllner, Eike Torben Menzel, Maria Kazim und Gürkan Deniz</p>
                </div>
            </body>
            </html>
            """;
}