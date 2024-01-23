package com.service.mailservice.services;

public class MailContentProvider {
    private MailContentProvider() {
    }

    public static final String DAILY_LEARN_REMINDER_DE = """
            <!DOCTYPE html>
                   <html lang="de">
                   <head>
                       <title></title>
                       <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                       <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                       <style>
                           * {
                               box-sizing: border-box;
                           }
                   
                           body {
                               margin: 0;
                               padding: 0;
                           }
                   
                           a[x-apple-data-detectors] {
                               color: inherit !important;
                               text-decoration: inherit !important;
                           }
                   
                           #MessageViewBody a {
                               color: inherit;
                               text-decoration: none;
                           }
                   
                           p {
                               line-height: inherit
                           }
                   
                           .desktop_hide,
                           .desktop_hide table {
                               mso-hide: all;
                               display: none;
                               max-height: 0px;
                               overflow: hidden;
                           }
                   
                           .image_block img + div {
                               display: none;
                           }
                   
                           @media (max-width: 620px) {
                               .desktop_hide table.icons-inner {
                                   display: inline-block !important;
                               }
                   
                               .icons-inner {
                                   text-align: center;
                               }
                   
                               .icons-inner td {
                                   margin: 0 auto;
                               }
                   
                               .mobile_hide {
                                   display: none;
                               }
                   
                               .row-content {
                                   width: 100% !important;
                               }
                   
                               .stack .column {
                                   width: 100%;
                                   display: block;
                               }
                   
                               .mobile_hide {
                                   min-height: 0;
                                   max-height: 0;
                                   max-width: 0;
                                   overflow: hidden;
                                   font-size: 0px;
                               }
                   
                               .desktop_hide,
                               .desktop_hide table {
                                   display: table !important;
                                   max-height: none !important;
                               }
                   
                               .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                               .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                                   font-size: 15px !important;
                               }
                   
                               .row-2 .column-1 .block-1.paragraph_block td.pad,
                               .row-2 .column-1 .block-3.paragraph_block td.pad {
                                   padding: 15px 20px !important;
                               }
                   
                               .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                                   font-size: 12px !important;
                               }
                   
                               .row-3 .column-1 .block-1.paragraph_block td.pad {
                                   padding: 10px 20px !important;
                               }
                   
                               .row-2 .column-1 {
                                   padding: 15px 0 !important;
                               }
                           }
                       </style>
                   </head>
                   <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
                   <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                          style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                          width="100%">
                       <tbody>
                       <tr>
                           <td>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad">
                                                                   <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 30px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 38.4px;">
                                                                       <strong>Tägliches Lernabenteuer erwartet Dich</strong></h1>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                   <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                       <p style="margin: 0; margin-bottom: 16px;">Hallo ${username},</p>
                                                                       <p style="margin: 0; margin-bottom: 16px;">Neuer Tag, neue
                                                                           Möglichkeiten! Es ist an der Zeit, die Gehirnzellen zu
                                                                           aktivieren und deinen Verstand zum Strahlen zu bringen! 💡</p>
                                                                       <p style="margin: 0;">Klicke auf die Schaltfläche unten und lege
                                                                           sofort los:</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                       <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                                   <div align="center" class="alignment"><!--[if mso]>
                                                                       <v:roundrect xmlns:v="urn:schemas-microsoft-com:vml"
                                                                                    xmlns:w="urn:schemas-microsoft-com:office:word"
                                                                                    href="https://www.example.com"
                                                                                    style="height:39px;width:192px;v-text-anchor:middle;"
                                                                                    arcsize="13%" stroke="false" fillcolor="#5bc560">
                                                                           <w:anchorlock/>
                                                                           <v:textbox inset="0px,0px,0px,0px">
                                                                               <center style="color:#ecefe6; font-family:Tahoma, Verdana, sans-serif; font-size:22px">
                                                                       <![endif]--><a href="${dailyLearnUrl}"
                                                                                      style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                                      target="_blank"><span
                                                                               style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                               style="word-break: break-word; line-height: 33px;">Lernen beginnen</span></span></a>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                   <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                       <p style="margin: 0;">Viel Liebe und Spaß!<br/>Simon von Cards
                                                                           Trainer<br/><br/>PS. Wenn Sie diese E-Mail überrascht hat, keine
                                                                           Sorge, ignorieren sie einfach!</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                                   <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                       <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                           von</p>
                                                                       <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                           Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                                   <table cellpadding="0" cellspacing="0" role="presentation"
                                                                          style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                          width="100%">
                                                                       <tr>
                                                                           <td class="alignment"
                                                                               style="vertical-align: middle; text-align: center;">
                                                                               <!--[if vml]>
                                                                               <table align="center" cellpadding="0" cellspacing="0"
                                                                                      role="presentation"
                                                                                      style="display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;">
                                                                               <![endif]-->
                                                                           </td>
                                                                       </tr>
                                                                   </table>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                           </td>
                       </tr>
                       </tbody>
                   </table><!-- End -->
                   </body>
                   </html>
                   """;

    public static final String DAILY_LEARN_REMINDER_EN = """
                <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <title></title>
                            <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                            <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                            <style>
                                * {
                                    box-sizing: border-box;
                                }
                        
                                body {
                                    margin: 0;
                                    padding: 0;
                                }
                        
                                a[x-apple-data-detectors] {
                                    color: inherit !important;
                                    text-decoration: inherit !important;
                                }
                        
                                #MessageViewBody a {
                                    color: inherit;
                                    text-decoration: none;
                                }
                        
                                p {
                                    line-height: inherit
                                }
                        
                                .desktop_hide,
                                .desktop_hide table {
                                    mso-hide: all;
                                    display: none;
                                    max-height: 0px;
                                    overflow: hidden;
                                }
                        
                                .image_block img + div {
                                    display: none;
                                }
                        
                                @media (max-width: 620px) {
                                    .desktop_hide table.icons-inner {
                                        display: inline-block !important;
                                    }
                        
                                    .icons-inner {
                                        text-align: center;
                                    }
                        
                                    .icons-inner td {
                                        margin: 0 auto;
                                    }
                        
                                    .mobile_hide {
                                        display: none;
                                    }
                        
                                    .row-content {
                                        width: 100% !important;
                                    }
                        
                                    .stack .column {
                                        width: 100%;
                                        display: block;
                                    }
                        
                                    .mobile_hide {
                                        min-height: 0;
                                        max-height: 0;
                                        max-width: 0;
                                        overflow: hidden;
                                        font-size: 0px;
                                    }
                        
                                    .desktop_hide,
                                    .desktop_hide table {
                                        display: table !important;
                                        max-height: none !important;
                                    }
                        
                                    .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                                    .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                                        font-size: 15px !important;
                                    }
                        
                                    .row-2 .column-1 .block-1.paragraph_block td.pad,
                                    .row-2 .column-1 .block-3.paragraph_block td.pad {
                                        padding: 15px 20px !important;
                                    }
                        
                                    .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                                        font-size: 12px !important;
                                    }
                        
                                    .row-3 .column-1 .block-1.paragraph_block td.pad {
                                        padding: 10px 20px !important;
                                    }
                        
                                    .row-2 .column-1 {
                                        padding: 15px 0 !important;
                                    }
                                }
                            </style>
                        </head>
                        <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
                        <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                               width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                                       width="600">
                                                    <tbody>
                                                    <tr>
                                                        <td class="column column-1"
                                                            style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                            width="100%">
                                                            <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                                   role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad">
                                                                        <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 32px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 38.4px;">
                                                                            <strong>Daily Learning Adventure awaits</strong></h1>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                       width="600">
                                                    <tbody>
                                                    <tr>
                                                        <td class="column column-1"
                                                            style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                            width="100%">
                                                            <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                                   role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad"
                                                                        style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                        <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                            <p style="margin: 0; margin-bottom: 16px;">Hello ${username}</p>
                                                                            <p style="margin: 0; margin-bottom: 16px;">New day, new
                                                                                possibilities! Time to activate those brain cells and make your
                                                                                mind shine! 💡</p>
                                                                            <p style="margin: 0;"><strong>Click the button below and get started
                                                                                right away! </strong></p>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                                   role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad"
                                                                        style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                                        <div align="center" class="alignment">
                                                                            <a href="${dailyLearnUrl}"
                                                                               style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                               target="_blank"><span
                                                                                    style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                                    style="word-break: break-word; line-height: 33px;">Start Learning</span></span></a>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                            <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                                   role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad"
                                                                        style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                        <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                            <p style="margin: 0;">Much love and fun!<br/>Simon from Cards
                                                                                Trainer<br/><br/>PS. If this email caught you by surprise, no
                                                                                worries, just ignore it!</p>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                       width="600">
                                                    <tbody>
                                                    <tr>
                                                        <td class="column column-1"
                                                            style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                            width="100%">
                                                            <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                                   role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad"
                                                                        style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                                        <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                            <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                                von</p>
                                                                            <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                                Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                                        <tbody>
                                        <tr>
                                            <td>
                                                <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                                       width="600">
                                                    <tbody>
                                                    <tr>
                                                        <td class="column column-1"
                                                            style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                            width="100%">
                                                            <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                                   role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="pad"
                                                                        style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                                        <table cellpadding="0" cellspacing="0" role="presentation"
                                                                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                               width="100%">
                                                                            <tr>
                                                                                <td class="alignment"
                                                                                    style="vertical-align: middle; text-align: center;">
                                                                                    <!--[if vml]>
                                                                                    <table align="center" cellpadding="0" cellspacing="0"
                                                                                           role="presentation"
                                                                                           style="display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;">
                                                                                    <![endif]-->
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table><!-- End -->
                        </body>
                        </html>
            """;


    public static final String MAIL_PASSWORD_RESET_DE = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <title></title>
                <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                <style>
                    * {
                        box-sizing: border-box;
                    }
                       
                    body {
                        margin: 0;
                        padding: 0;
                    }
                       
                    a[x-apple-data-detectors] {
                        color: inherit !important;
                        text-decoration: inherit !important;
                    }
                       
                    #MessageViewBody a {
                        color: inherit;
                        text-decoration: none;
                    }
                       
                    p {
                        line-height: inherit
                    }
                       
                    .desktop_hide,
                    .desktop_hide table {
                        mso-hide: all;
                        display: none;
                        max-height: 0px;
                        overflow: hidden;
                    }
                       
                    .image_block img + div {
                        display: none;
                    }
                       
                    @media (max-width: 620px) {
                        .desktop_hide table.icons-inner {
                            display: inline-block !important;
                        }
                       
                        .icons-inner {
                            text-align: center;
                        }
                       
                        .icons-inner td {
                            margin: 0 auto;
                        }
                       
                        .mobile_hide {
                            display: none;
                        }
                       
                        .row-content {
                            width: 100% !important;
                        }
                       
                        .stack .column {
                            width: 100%;
                            display: block;
                        }
                       
                        .mobile_hide {
                            min-height: 0;
                            max-height: 0;
                            max-width: 0;
                            overflow: hidden;
                            font-size: 0px;
                        }
                       
                        .desktop_hide,
                        .desktop_hide table {
                            display: table !important;
                            max-height: none !important;
                        }
                       
                        .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                        .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                            font-size: 15px !important;
                        }
                       
                        .row-2 .column-1 .block-1.paragraph_block td.pad,
                        .row-2 .column-1 .block-3.paragraph_block td.pad {
                            padding: 15px 20px !important;
                        }
                       
                        .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                            font-size: 12px !important;
                        }
                       
                        .row-3 .column-1 .block-1.paragraph_block td.pad {
                            padding: 10px 20px !important;
                        }
                       
                        .row-2 .column-1 {
                            padding: 15px 0 !important;
                        }
                    }
                </style>
            </head>
            <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
            <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                   width="100%">
                <tbody>
                <tr>
                    <td>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad">
                                                            <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 29px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 34.8px;">
                                                                <strong>Sicher auf Deiner Reise: Passwort zurücksetzen </strong>
                                                            </h1>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Hallo ${username},</p>
                                                                <p style="margin: 0; margin-bottom: 16px;">Das Leben kann hektisch
                                                                    sein und manchmal fallen Passwörter durch die Maschen.</p>
                                                                <p style="margin: 0;">Du möchtest Dein Passwort zurücksetzen? Klicke
                                                                    einfach auf die Schaltfläche unten, um ein neues Passwort zu
                                                                    vergeben:</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                            <div align="center" class="alignment">
                                                                <![endif]--><a href="${resetUrl}"
                                                                               style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                               target="_blank"><span
                                                                    style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                    style="word-break: break-word; line-height: 33px;">Passwort zurücksetzen</span></span></a>
                                                                <!--[if mso]></center></v:textbox></v:roundrect><![endif]--></div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0;">Bleib auf der sicheren Seite!<br/>Simon von
                                                                    Cards Trainer<br/><br/>PS. Wenn diese E-Mail Dich überrascht
                                                                    hat, ignoriere sie einfach! Als zusätzliche Sicherheitsmaßnahme
                                                                    wäre es jedoch eine gute Idee, Dein Passwort auf unserer Website
                                                                    zurückzusetzen.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                            <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                    von</p>
                                                                <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                    Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                            <table cellpadding="0" cellspacing="0" role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="alignment"
                                                                        style="vertical-align: middle; text-align: center;">
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table><!-- End -->
            </body>
            </html>
            """;

    public static final String MAIL_PASSWORD_RESET_EN = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <title></title>
                <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                <style>
                    * {
                        box-sizing: border-box;
                    }
                        
                    body {
                        margin: 0;
                        padding: 0;
                    }
                        
                    a[x-apple-data-detectors] {
                        color: inherit !important;
                        text-decoration: inherit !important;
                    }
                        
                    #MessageViewBody a {
                        color: inherit;
                        text-decoration: none;
                    }
                        
                    p {
                        line-height: inherit
                    }
                        
                    .desktop_hide,
                    .desktop_hide table {
                        mso-hide: all;
                        display: none;
                        max-height: 0px;
                        overflow: hidden;
                    }
                        
                    .image_block img + div {
                        display: none;
                    }
                        
                    @media (max-width: 620px) {
                        .desktop_hide table.icons-inner {
                            display: inline-block !important;
                        }
                        
                        .icons-inner {
                            text-align: center;
                        }
                        
                        .icons-inner td {
                            margin: 0 auto;
                        }
                        
                        .mobile_hide {
                            display: none;
                        }
                        
                        .row-content {
                            width: 100% !important;
                        }
                        
                        .stack .column {
                            width: 100%;
                            display: block;
                        }
                        
                        .mobile_hide {
                            min-height: 0;
                            max-height: 0;
                            max-width: 0;
                            overflow: hidden;
                            font-size: 0px;
                        }
                        
                        .desktop_hide,
                        .desktop_hide table {
                            display: table !important;
                            max-height: none !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                        .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                            font-size: 15px !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad,
                        .row-2 .column-1 .block-3.paragraph_block td.pad {
                            padding: 15px 20px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                            font-size: 12px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad {
                            padding: 10px 20px !important;
                        }
                        
                        .row-2 .column-1 {
                            padding: 15px 0 !important;
                        }
                    }
                </style>
            </head>
            <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
            <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                   width="100%">
                <tbody>
                <tr>
                    <td>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad">
                                                            <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 29px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 34.8px;">
                                                                <strong>Secure Your Journey: Password Reset </strong></h1>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Hello ${username},</p>
                                                                <p style="margin: 0; margin-bottom: 16px;">Life can be hectic, and
                                                                    sometimes passwords slip through the cracks.</p>
                                                                <p style="margin: 0;">Need a password reset? Just click the button
                                                                    below to assign a new password:</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                            <div align="center" class="alignment">
                                                                <a href="${resetUrl}"
                                                                   style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                   target="_blank"><span
                                                                        style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                        style="word-break: break-word; line-height: 33px;">Reset Password</span></span></a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0;">Stay secure and take your time!<br/>Simon from
                                                                    Cards Trainer<br/><br/>PS. If this email caught you by surprise,
                                                                    no worries, just ignore it! However, for an extra layer of
                                                                    security, it might be a good idea to initiate a password reset
                                                                    directly on our website.</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                            <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                    von</p>
                                                                <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                    Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                            <table cellpadding="0" cellspacing="0" role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="alignment"
                                                                        style="vertical-align: middle; text-align: center;">
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table><!-- End -->
            </body>
            </html>
            """;

    public static final String MAIL_VERIFICATION = """
            <!DOCTYPE html>
                   <html lang="en">
                   <head>
                       <title></title>
                       <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                       <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                       <style>
                           * {
                               box-sizing: border-box;
                           }
                   
                           body {
                               margin: 0;
                               padding: 0;
                           }
                   
                           a[x-apple-data-detectors] {
                               color: inherit !important;
                               text-decoration: inherit !important;
                           }
                   
                           #MessageViewBody a {
                               color: inherit;
                               text-decoration: none;
                           }
                   
                           p {
                               line-height: inherit
                           }
                   
                           .desktop_hide,
                           .desktop_hide table {
                               mso-hide: all;
                               display: none;
                               max-height: 0px;
                               overflow: hidden;
                           }
                   
                           .image_block img + div {
                               display: none;
                           }
                   
                           @media (max-width: 620px) {
                               .desktop_hide table.icons-inner {
                                   display: inline-block !important;
                               }
                   
                               .icons-inner {
                                   text-align: center;
                               }
                   
                               .icons-inner td {
                                   margin: 0 auto;
                               }
                   
                               .mobile_hide {
                                   display: none;
                               }
                   
                               .row-content {
                                   width: 100% !important;
                               }
                   
                               .stack .column {
                                   width: 100%;
                                   display: block;
                               }
                   
                               .mobile_hide {
                                   min-height: 0;
                                   max-height: 0;
                                   max-width: 0;
                                   overflow: hidden;
                                   font-size: 0px;
                               }
                   
                               .desktop_hide,
                               .desktop_hide table {
                                   display: table !important;
                                   max-height: none !important;
                               }
                   
                               .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                               .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                                   font-size: 15px !important;
                               }
                   
                               .row-2 .column-1 .block-1.paragraph_block td.pad,
                               .row-2 .column-1 .block-3.paragraph_block td.pad {
                                   padding: 15px 20px !important;
                               }
                   
                               .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                                   font-size: 12px !important;
                               }
                   
                               .row-3 .column-1 .block-1.paragraph_block td.pad {
                                   padding: 10px 20px !important;
                               }
                   
                               .row-2 .column-1 {
                                   padding: 15px 0 !important;
                               }
                           }
                       </style>
                   </head>
                   <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
                   <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                          style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                          width="100%">
                       <tbody>
                       <tr>
                           <td>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad">
                                                                   <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 24px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 28.799999999999997px;">
                                                                       <strong>Welcome to the Family: Confirm Your Identity</strong></h1>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                   <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                       <p style="margin: 0; margin-bottom: 16px;">Hello ${username},</p>
                                                                       <p style="margin: 0; margin-bottom: 16px;">You've received this
                                                                           email to confirm your identity and unlock the full potential of
                                                                           your account.</p>
                                                                       <p style="margin: 0;">To embark on this exciting journey, click the
                                                                           button below:</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                       <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                                   <div align="center" class="alignment">
                                                                       <a href="${verificationUrl}"
                                                                          style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                          target="_blank"><span
                                                                               style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                               style="word-break: break-word; line-height: 33px;">Confirm</span></span></a>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                                   <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                       <p style="margin: 0;">Nice to have you with us!<br/>Simon from Cards
                                                                           Trainer<br/><br/>PS. If this email caught you by surprise, no
                                                                           worries, just ignore it!</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                              role="presentation"
                                                              style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                                   <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                       <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                           von</p>
                                                                       <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                           Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                                   </div>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                               <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                                      style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                                   <tbody>
                                   <tr>
                                       <td>
                                           <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                                  role="presentation"
                                                  style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                                  width="600">
                                               <tbody>
                                               <tr>
                                                   <td class="column column-1"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                       width="100%">
                                                       <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                              role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                              width="100%">
                                                           <tr>
                                                               <td class="pad"
                                                                   style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                                   <table cellpadding="0" cellspacing="0" role="presentation"
                                                                          style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                          width="100%">
                                                                       <tr>
                                                                           <td class="alignment"
                                                                               style="vertical-align: middle; text-align: center;">
                                                                           </td>
                                                                       </tr>
                                                                   </table>
                                                               </td>
                                                           </tr>
                                                       </table>
                                                   </td>
                                               </tr>
                                               </tbody>
                                           </table>
                                       </td>
                                   </tr>
                                   </tbody>
                               </table>
                           </td>
                       </tr>
                       </tbody>
                   </table><!-- End -->
                   </body>
                   </html>
            """;

    public static final String SHARE_DECK_DE = """
            <!DOCTYPE html>
            <html lang="de">
            <head>
                <title></title>
                <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                <style>
                    * {
                        box-sizing: border-box;
                    }
                        
                    body {
                        margin: 0;
                        padding: 0;
                    }
                        
                    a[x-apple-data-detectors] {
                        color: inherit !important;
                        text-decoration: inherit !important;
                    }
                        
                    #MessageViewBody a {
                        color: inherit;
                        text-decoration: none;
                    }
                        
                    p {
                        line-height: inherit
                    }
                        
                    .desktop_hide,
                    .desktop_hide table {
                        mso-hide: all;
                        display: none;
                        max-height: 0px;
                        overflow: hidden;
                    }
                        
                    .image_block img + div {
                        display: none;
                    }
                        
                    @media (max-width: 620px) {
                        .desktop_hide table.icons-inner {
                            display: inline-block !important;
                        }
                        
                        .icons-inner {
                            text-align: center;
                        }
                        
                        .icons-inner td {
                            margin: 0 auto;
                        }
                        
                        .mobile_hide {
                            display: none;
                        }
                        
                        .row-content {
                            width: 100% !important;
                        }
                        
                        .stack .column {
                            width: 100%;
                            display: block;
                        }
                        
                        .mobile_hide {
                            min-height: 0;
                            max-height: 0;
                            max-width: 0;
                            overflow: hidden;
                            font-size: 0px;
                        }
                        
                        .desktop_hide,
                        .desktop_hide table {
                            display: table !important;
                            max-height: none !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                        .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                            font-size: 15px !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad,
                        .row-2 .column-1 .block-3.paragraph_block td.pad {
                            padding: 15px 20px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                            font-size: 12px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad {
                            padding: 10px 20px !important;
                        }
                        
                        .row-2 .column-1 {
                            padding: 15px 0 !important;
                        }
                    }
                </style>
            </head>
            <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
            <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                   width="100%">
                <tbody>
                <tr>
                    <td>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad">
                                                            <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 20px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 28.799999999999997px;">
                                                                <strong>Geteilte Brillanz: Jemand möchte ein Deck mit Dir
                                                                    teilen</strong></h1>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Hallo ${username},</p>
                                                                <p style="margin: 0; margin-bottom: 16px;">Aufregende Neuigkeiten
                                                                    von ${senderName}: Ein Deck namens '${deckName}' wurde mit Dir
                                                                    geteilt!</p>
                                                                <p style="margin: 0;">Das Streben nach Wissen wurde gerade
                                                                    kollaborativ! Klicke unten, um das Deck zu integrieren:</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                            <div align="center" class="alignment">
                                                                <a href="${shareDeckUrl}"
                                                                   style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                   target="_blank"><span
                                                                        style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                        style="word-break: break-word; line-height: 33px;">Deck integrieren</span></span></a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0;">Viel Spaß beim Teilen und Erforschen!<br/>Simon
                                                                    von Cards Trainer<br/><br/>PS. Wenn Dich diese E-Mail überrascht
                                                                    hat, keine Sorge, ignoriere sie einfach!</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                            <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                    von</p>
                                                                <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                    Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                            <table cellpadding="0" cellspacing="0" role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="alignment"
                                                                        style="vertical-align: middle; text-align: center;">
                                                                        <!--[if vml]>
                                                                        <table align="center" cellpadding="0" cellspacing="0"
                                                                               role="presentation"
                                                                               style="display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;">
                                                                        <![endif]-->
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table><!-- End -->
            </body>
            </html>
            """;

    public static final String SHARE_DECK_EN = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <title></title>
                <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
                <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
                <style>
                    * {
                        box-sizing: border-box;
                    }
                        
                    body {
                        margin: 0;
                        padding: 0;
                    }
                        
                    a[x-apple-data-detectors] {
                        color: inherit !important;
                        text-decoration: inherit !important;
                    }
                        
                    #MessageViewBody a {
                        color: inherit;
                        text-decoration: none;
                    }
                        
                    p {
                        line-height: inherit
                    }
                        
                    .desktop_hide,
                    .desktop_hide table {
                        mso-hide: all;
                        display: none;
                        max-height: 0px;
                        overflow: hidden;
                    }
                        
                    .image_block img + div {
                        display: none;
                    }
                        
                    @media (max-width: 620px) {
                        .desktop_hide table.icons-inner {
                            display: inline-block !important;
                        }
                        
                        .icons-inner {
                            text-align: center;
                        }
                        
                        .icons-inner td {
                            margin: 0 auto;
                        }
                        
                        .mobile_hide {
                            display: none;
                        }
                        
                        .row-content {
                            width: 100% !important;
                        }
                        
                        .stack .column {
                            width: 100%;
                            display: block;
                        }
                        
                        .mobile_hide {
                            min-height: 0;
                            max-height: 0;
                            max-width: 0;
                            overflow: hidden;
                            font-size: 0px;
                        }
                        
                        .desktop_hide,
                        .desktop_hide table {
                            display: table !important;
                            max-height: none !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad > div,
                        .row-2 .column-1 .block-3.paragraph_block td.pad > div {
                            font-size: 15px !important;
                        }
                        
                        .row-2 .column-1 .block-1.paragraph_block td.pad,
                        .row-2 .column-1 .block-3.paragraph_block td.pad {
                            padding: 15px 20px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad > div {
                            font-size: 12px !important;
                        }
                        
                        .row-3 .column-1 .block-1.paragraph_block td.pad {
                            padding: 10px 20px !important;
                        }
                        
                        .row-2 .column-1 {
                            padding: 15px 0 !important;
                        }
                    }
                </style>
            </head>
            <body style="margin: 0; background-color: #ecefe6; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;">
            <table border="0" cellpadding="0" cellspacing="0" class="nl-container" role="presentation"
                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; background-image: none; background-position: top left; background-size: auto; background-repeat: no-repeat;"
                   width="100%">
                <tbody>
                <tr>
                    <td>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-1" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 10px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="10" cellspacing="0" class="heading_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad">
                                                            <h1 style="margin: 0; color: #5bc560; direction: ltr; font-family: Montserrat, Trebuchet MS, Lucida Grande, Lucida Sans Unicode, Lucida Sans, Tahoma, sans-serif; font-size: 20px; font-weight: 700; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0; mso-line-height-alt: 28.799999999999997px;">
                                                                <strong>Shared Brilliance: Someone wants to share a deck with
                                                                    you</strong></h1>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-2" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ecefe6; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 20px; vertical-align: middle; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Hello ${username},</p>
                                                                <p style="margin: 0; margin-bottom: 16px;">Exciting news from
                                                                    ${senderName}: A deck named '${deckName}' has been shared with
                                                                    you!</p>
                                                                <p style="margin: 0;">The pursuit of knowledge just got
                                                                    collaborative! Click below to seamlessly integrate the deck:</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="button_block block-2"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:20px;padding-right:20px;padding-top:10px;text-align:center;">
                                                            <div align="center" class="alignment">
                                                                <a href="${shareDeckUrl}"
                                                                   style="text-decoration:none;display:inline-block;color:#ecefe6;background-color:#5bc560;border-radius:5px;width:auto;border-top:0px solid transparent;font-weight:300;border-right:0px solid transparent;border-bottom:0px solid transparent;border-left:0px solid transparent;padding-top:3px;padding-bottom:3px;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:22px;text-align:center;mso-border-alt:none;word-break:keep-all;"
                                                                   target="_blank"><span
                                                                        style="padding-left:20px;padding-right:20px;font-size:22px;display:inline-block;letter-spacing:1px;"><span
                                                                        style="word-break: break-word; line-height: 33px;">Integrate Deck</span></span></a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-3"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:15px;padding-left:40px;padding-right:40px;padding-top:15px;">
                                                            <div style="color:#1e2e2a;direction:ltr;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:18px;font-weight:400;letter-spacing:1px;line-height:150%;text-align:left;mso-line-height-alt:27px;">
                                                                <p style="margin: 0;">Happy sharing and exploring!<br/>Simon from
                                                                    Cards Trainer<br/><br/>PS. If this email caught you by surprise,
                                                                    no worries, just ignore it!</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-3" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #5bc560; border-radius: 0; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 30px; padding-top: 30px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="paragraph_block block-1"
                                                       role="presentation"
                                                       style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="padding-bottom:10px;padding-left:40px;padding-right:40px;padding-top:10px;">
                                                            <div style="color:#ffffff;direction:ltr;font-family:'Ubuntu', Tahoma, Verdana, Segoe, sans-serif;font-size:12px;font-weight:400;letter-spacing:1px;line-height:120%;text-align:center;mso-line-height-alt:14.399999999999999px;">
                                                                <p style="margin: 0; margin-bottom: 16px;">Softwaretechnik Projekt
                                                                    von</p>
                                                                <p style="margin: 0;">Tim Martin Süllner<br/>Eike Torben
                                                                    Menzel<br/>Maria Kazim<br/>Gürkan Deniz</p>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <table align="center" border="0" cellpadding="0" cellspacing="0" class="row row-4" role="presentation"
                               style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff;" width="100%">
                            <tbody>
                            <tr>
                                <td>
                                    <table align="center" border="0" cellpadding="0" cellspacing="0" class="row-content stack"
                                           role="presentation"
                                           style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #ffffff; color: #000000; width: 600px; margin: 0 auto;"
                                           width="600">
                                        <tbody>
                                        <tr>
                                            <td class="column column-1"
                                                style="mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; padding-bottom: 5px; padding-top: 5px; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;"
                                                width="100%">
                                                <table border="0" cellpadding="0" cellspacing="0" class="icons_block block-1"
                                                       role="presentation" style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                       width="100%">
                                                    <tr>
                                                        <td class="pad"
                                                            style="vertical-align: middle; color: #1e0e4b; font-family: 'Inter', sans-serif; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;">
                                                            <table cellpadding="0" cellspacing="0" role="presentation"
                                                                   style="mso-table-lspace: 0pt; mso-table-rspace: 0pt;"
                                                                   width="100%">
                                                                <tr>
                                                                    <td class="alignment"
                                                                        style="vertical-align: middle; text-align: center;">
                                                                        <!--[if vml]>
                                                                        <table align="center" cellpadding="0" cellspacing="0"
                                                                               role="presentation"
                                                                               style="display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;">
                                                                        <![endif]-->
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table><!-- End -->
            </body>
            </html>
            """;
}
