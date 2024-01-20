import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HttpBackend, HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

export function TranslateLoaderFactory(httpClient: HttpClient) {
  return  new  TranslateHttpLoader(httpClient, './assets/i18n/', '.json');
}

export const provideTranslation = () => ({
  defaultLanguage: 'en',
  loader: {
    provide: TranslateLoader,
    useFactory: TranslateLoaderFactory,
    deps: [HttpClient],
  },
});

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
  importProvidersFrom([
    HttpClientModule,
    TranslateModule.forRoot(provideTranslation())
  ]),
  ]
};
