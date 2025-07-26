module.exports = {
  preset: "ts-jest",
  testEnvironment: "node",
  roots: ["<rootDir>/test"],
  testMatch: ["<rootDir>/test/**/*.spec.ts"],
  moduleNameMapper: {
    "^@test/(.*)$": "<rootDir>/test/$1",
    "^@/(.*)$": "<rootDir>/src/$1"
  },
  moduleFileExtensions: ["js", "json", "ts"],
  transform: {
    "^.+\\.(t|j)s$": "ts-jest"
  },
  collectCoverage: true,
  collectCoverageFrom: ["<rootDir>/src/**/*.{ts,js}"],
  coveragePathIgnorePatterns: ["/test/", "/node_modules/", "/dist/"],
  coverageDirectory: "./artifacts/test-report/coverage",
  fakeTimers: {
    enableGlobally: false
  },
  setupFilesAfterEnv: ["<rootDir>/test/jest-unit.setup.ts"],
  verbose: true
};
