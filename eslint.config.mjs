// @ts-check
import eslint from "@eslint/js";
import eslintPluginPrettierRecommended from "eslint-plugin-prettier/recommended";
import sonarjs from "eslint-plugin-sonarjs";
import spellcheck from "eslint-plugin-spellcheck";
import globals from "globals";
import tseslint from "typescript-eslint";

export default tseslint.config(
  {
    ignores: [
      "eslint.config.mjs",
      "**/.nyc_output/",
      "**/.vscode/",
      "**/coverage/",
      "**/bundle/",
      "**/dist/",
      "**/node_modules/",
      "**/Development/",
      "**/test/"
    ]
  },
  eslint.configs.recommended,
  ...tseslint.configs.strictTypeChecked,
  eslintPluginPrettierRecommended,
  sonarjs.configs.recommended,
  {
    languageOptions: {
      globals: {
        ...globals.node
      },
      parserOptions: {
        ecmaVersion: 2022,
        sourceType: "module",
        projectService: true,
        tsconfigRootDir: import.meta.dirname
      }
    }
  },
  {
    plugins: {
      spellcheck: spellcheck
    },
    rules: {
      "@typescript-eslint/no-unused-expressions": "error",
      "@typescript-eslint/no-extraneous-class": "error",
      "@typescript-eslint/consistent-type-imports": "error",
      "@typescript-eslint/init-declarations": "error",
      "@typescript-eslint/no-explicit-any": "error",
      "@typescript-eslint/restrict-template-expressions": "off",
      "sonarjs/todo-tag": "off",
      "sonarjs/no-clear-text-protocols": "off",
      "sonarjs/no-ignored-exceptions": "off",
      "sonarjs/no-exclusive-tests": "off",
      "spellcheck/spell-checker": "off",
      "prettier/prettier": [
        "error",
        {
          endOfLine: "auto"
        }
      ]
    }
  }
);
