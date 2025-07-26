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
      "jest.config.js"
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
        project: "./tsconfig.json",
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
      "@typescript-eslint/interface-name-prefix": "off",
      "@typescript-eslint/no-inferrable-types": "off",
      "@typescript-eslint/prefer-readonly-parameter-types": "off",
      "@typescript-eslint/restrict-template-expressions": "off",
      "@typescript-eslint/require-await": "off",
      "@typescript-eslint/no-type-alias": "off",
      "@typescript-eslint/no-magic-numbers": "off",
      "@typescript-eslint/no-extraneous-class": "error",
      "@typescript-eslint/unbound-method": "off",
      "@typescript-eslint/naming-convention": "off",
      "@typescript-eslint/no-non-null-assertion": "off",
      "@typescript-eslint/ban-tslint-comment": "off",
      "@typescript-eslint/no-confusing-void-expression": "off",
      "@typescript-eslint/max-params": "off",
      "@typescript-eslint/prefer-destructuring": "off",
      "@typescript-eslint/non-nullable-type-assertion-style": "off",
      "@typescript-eslint/no-misused-spread": "off",
      "@typescript-eslint/consistent-type-imports": "error",
      "@typescript-eslint/init-declarations": "error",
      "@typescript-eslint/no-explicit-any": "error",
      "no-console": "off",
      "sort-imports": "off",
      "sonarjs/no-nested-functions": "off",
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
