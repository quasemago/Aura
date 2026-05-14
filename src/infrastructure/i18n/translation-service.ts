import { Settings } from "@/infrastructure/config/settings";
import * as Types from "@/infrastructure/config/types";
import i18next from "i18next";
import { inject, injectable, postConstruct } from "inversify";
import { readFileSync } from "node:fs";
import { Logger } from "winston";

@injectable()
export class TranslationService {
  private readonly DEFAULT_NAMESPACE = "translation";

  constructor(
    @inject(Settings) private readonly settings: Settings,
    @inject(Types.Logger) private readonly logger: Logger
  ) {}

  public t(key: string, options?: Record<string, string | number>): string {
    const language = this.settings.getBotDefaultLanguage();
    this.loadLanguage(language);

    return i18next.t(key, {
      lng: language,
      ns: this.DEFAULT_NAMESPACE,
      ...options
    });
  }

  @postConstruct()
  protected initialize(): void {
    if (i18next.isInitialized) {
      return;
    }

    const defaultLanguage = this.settings.getBotDefaultLanguage();
    const defaultTranslations = this.readLanguage(defaultLanguage) ?? {};
    i18next
      .init({
        initAsync: false,
        lng: defaultLanguage,
        fallbackLng: defaultLanguage,
        defaultNS: this.DEFAULT_NAMESPACE,
        resources: {
          [defaultLanguage]: {
            [this.DEFAULT_NAMESPACE]: defaultTranslations
          }
        },
        interpolation: {
          escapeValue: false
        }
      })
      .catch((error: unknown) => {
        this.logger.error("Failed to initialize i18next", error as Error);
      });
  }

  private loadLanguage(language: string): void {
    if (i18next.hasResourceBundle(language, this.DEFAULT_NAMESPACE)) {
      return;
    }

    const translations = this.readLanguage(language);
    if (translations !== undefined) {
      i18next.addResourceBundle(language, this.DEFAULT_NAMESPACE, translations, true, true);
    }
  }

  private readLanguage(language: string): Record<string, string> | undefined {
    try {
      const localeUrl = new URL(`../../locales/${language}.json`, import.meta.url);
      return JSON.parse(readFileSync(localeUrl, "utf8")) as Record<string, string>;
    } catch (error: unknown) {
      this.logger.error(`Failed to read locale "${language}".`, error as Error);
      return undefined;
    }
  }
}
