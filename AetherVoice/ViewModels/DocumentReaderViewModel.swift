import Foundation
import AVFoundation
import NaturalLanguage

@MainActor
class DocumentReaderViewModel: ObservableObject, SpeechSynthesizerDelegate {

    @Published var currentSentenceIndex = 0
    @Published var isSpeaking = false
    @Published var errorMessage: String?
    
    @Published var selectedTTSService: TTSService {
        didSet {
            stopReadingText()
            updateSelectedSynthesizer()
        }
    }
    @Published var selectedEngine: String = defaultTTSSettings().1 {
        didSet {
            stopReadingText()
            updateVoicesForSelectedEngine()
        }
    }
    @Published var selectedLanguage: String = defaultTTSSettings().2 {
        didSet {
            stopReadingText()
            updateVoicesForSelectedLanguage()
        }
    }
    @Published var selectedVoice: String = defaultTTSSettings().3 {
        didSet {
            stopReadingText()
            updateSynthesizerVoice()
        }
    }
    @Published var availableSynthesizers: [TTSService]
    @Published var availableLanguages: [String] = []
    @Published var availableVoices: [String] = []
    @Published var availableEngines: [String] = []

    let document: AppDocument
    var sentences: [String]
    var synthesizerDict: [TTSService: SpeechSynthesizerProtocol]
    var speechSynthesizer: SpeechSynthesizerProtocol
    
    init(document: AppDocument, synthesizerDict: [TTSService: SpeechSynthesizerProtocol]) {
        self.document = document
        self.synthesizerDict = synthesizerDict
        self.sentences = DocumentReaderViewModel.splitIntoSentences(text: document.content)
        self.availableSynthesizers = self.synthesizerDict.map { $0.key }
        let loadedSettings = DocumentReaderViewModel.loadTTSSettings()
        print("LOADED Settings: \(loadedSettings)")
        if loadedSettings.0 != nil && loadedSettings.1 != nil && loadedSettings.2 != nil && loadedSettings.3 != nil {
            self.selectedTTSService = loadedSettings.0!
            self.selectedEngine = loadedSettings.1!
            self.selectedLanguage = loadedSettings.2!
            self.selectedVoice = loadedSettings.3!
            self.speechSynthesizer = self.synthesizerDict[loadedSettings.0!]!
            self.speechSynthesizer.setDelegate(delegate: self)
            self.availableEngines = self.speechSynthesizer.supportedEngines()
            self.speechSynthesizer.setEngine(engine: selectedEngine)
            self.availableLanguages = self.speechSynthesizer.supportedLanguages()
            self.speechSynthesizer.setLanguage(language: selectedLanguage)
            self.availableVoices = self.speechSynthesizer.supportedVoices()
            self.speechSynthesizer.setVoice(voice: selectedVoice)
            print("Preloaded previous settings")
        } else {
            (selectedTTSService, selectedEngine, selectedLanguage, selectedVoice) = DocumentReaderViewModel.defaultTTSSettings()
            self.speechSynthesizer = self.synthesizerDict[.local]!
            self.speechSynthesizer.setDelegate(delegate: self)
            self.availableEngines = self.speechSynthesizer.supportedEngines()
            self.speechSynthesizer.setEngine(engine: selectedEngine)
            self.availableLanguages = self.speechSynthesizer.supportedLanguages()
            self.speechSynthesizer.setLanguage(language: selectedLanguage)
            self.availableVoices = self.speechSynthesizer.supportedVoices()
            self.speechSynthesizer.setVoice(voice: selectedVoice)
            saveTTSSettings()
        }
        if validateSelectedOptions() == false {
            print("Settings not valid: \(selectedTTSService) \(selectedEngine) \(selectedLanguage) \(selectedVoice)")
            (self.selectedTTSService, self.selectedEngine, self.selectedLanguage, self.selectedVoice) = DocumentReaderViewModel.defaultTTSSettings()
            updateSelectedSynthesizer()
        }
    }

    func startReading() async {
        guard currentSentenceIndex < sentences.count else { return }
        let sentenceToRead = sentences[currentSentenceIndex]
        do {
            try await speechSynthesizer.speak(text: sentenceToRead)
        } catch {
            didEncounterError(error)
        }
    }

    func stopReadingText() {
        DispatchQueue.main.async {
            self.isSpeaking = false
        }
        speechSynthesizer.stopSpeaking()
    }

    func toggleSpeech() {
        if self.isSpeaking == true {
            DispatchQueue.main.async {
                self.isSpeaking = false
            }
        } else {
            DispatchQueue.main.async {
                self.isSpeaking = true
            }
        }
    }

    func moveToPreviousSentence() {
        DispatchQueue.main.async {
            self.isSpeaking = false
            self.currentSentenceIndex = max(self.currentSentenceIndex - 1, 0)
            self.isSpeaking = true
        }
    }

    func moveToNextSentence() {
        if currentSentenceIndex < sentences.count - 1 {
            DispatchQueue.main.async {
                self.isSpeaking = false
                self.currentSentenceIndex += 1
                self.isSpeaking = true
            }
        }
    }

    private func updateSelectedSynthesizer() {
        self.speechSynthesizer = self.synthesizerDict[selectedTTSService]!
        self.speechSynthesizer.setDelegate(delegate: self)
        self.availableEngines = self.speechSynthesizer.supportedEngines()
        self.selectedEngine = self.availableEngines.first ?? ""
        self.speechSynthesizer.setEngine(engine: selectedEngine)
        self.availableLanguages = self.speechSynthesizer.supportedLanguages()
        self.selectedLanguage = self.availableLanguages.first ?? ""
        self.speechSynthesizer.setLanguage(language: selectedLanguage)
        self.availableVoices = self.speechSynthesizer.supportedVoices()
        self.selectedVoice = self.availableVoices.first ?? ""
        self.speechSynthesizer.setVoice(voice: selectedVoice)
        saveTTSSettings()
    }
    
    private func updateVoicesForSelectedEngine() {
        availableLanguages = speechSynthesizer.supportedLanguages()
        selectedLanguage = availableLanguages.first ?? ""
        speechSynthesizer.setEngine(engine: selectedEngine)
        availableVoices = speechSynthesizer.supportedVoices()
        selectedVoice = availableVoices.first ?? ""
        speechSynthesizer.setVoice(voice: selectedVoice)
        saveTTSSettings()
    }
    
    private func updateVoicesForSelectedLanguage() {
        speechSynthesizer.setLanguage(language: selectedLanguage)
        availableVoices = speechSynthesizer.supportedVoices()
        selectedVoice = availableVoices.first ?? ""
        speechSynthesizer.setVoice(voice: selectedVoice)
        saveTTSSettings()
    }

    private func updateSynthesizerVoice() {
        speechSynthesizer.setVoice(voice: selectedVoice)
        saveTTSSettings()
    }

    static func splitIntoSentences(text: String) -> [String] {
        var sentences = [String]()
        let cleanedText = text.replacingOccurrences(of: "\n", with: " ")
                             .replacingOccurrences(of: "\r", with: " ")

        var currentSentence = ""
        var insideQuotes = false

        for character in cleanedText {
            if character == "\"" || character == "「" {
                if !insideQuotes && !currentSentence.trimmingCharacters(in: .whitespaces).isEmpty {
                    sentences.append(currentSentence.trimmingCharacters(in: .whitespaces))
                    currentSentence = ""
                }
                insideQuotes.toggle()
            } else if character == "」" || (character == "\"" && insideQuotes) {
                insideQuotes = false
                currentSentence.append(character)
                sentences.append(currentSentence.trimmingCharacters(in: .whitespaces))
                currentSentence = ""
                continue
            }

            if (character == "." || character == "。"), !insideQuotes {
                currentSentence.append(character)
                sentences.append(currentSentence.trimmingCharacters(in: .whitespaces))
                currentSentence = ""
            } else {
                currentSentence.append(character)
            }
        }

        if !currentSentence.trimmingCharacters(in: .whitespaces).isEmpty {
            sentences.append(currentSentence.trimmingCharacters(in: .whitespaces))
        }

        return sentences.filter { !$0.isEmpty }
    }
    
    nonisolated func didFinishSpeaking() {
        DispatchQueue.main.async {
            print("Finished speaking \(self.currentSentenceIndex)")
            self.currentSentenceIndex += 1
        }
    }
    
    nonisolated func didEncounterError(_ error: Error) {
        // Handle different error cases
        DispatchQueue.main.async {
            self.errorMessage = error.localizedDescription
        }
    }
    
    func saveTTSSettings() {
        UserDefaults.standard.set(selectedTTSService.rawValue, forKey: "selectedTTSService")
        UserDefaults.standard.set(selectedEngine, forKey: "selectedEngine")
        UserDefaults.standard.set(selectedLanguage, forKey: "selectedLanguage")
        UserDefaults.standard.set(selectedVoice, forKey: "selectedVoice")
    }

    static func loadTTSSettings() -> (TTSService?, String?, String?, String?) {
        let loadedTTSService: TTSService?, loadedEngine: String?, loadedLanguage: String?, loadedVoice: String?
        if let serviceRawValue = UserDefaults.standard.string(forKey: "selectedTTSService"),
           let service = TTSService(rawValue: serviceRawValue) {
            loadedTTSService = service
        } else {
            loadedTTSService = nil
        }
        loadedEngine = UserDefaults.standard.string(forKey: "selectedEngine")
        loadedLanguage = UserDefaults.standard.string(forKey: "selectedLanguage")
        loadedVoice = UserDefaults.standard.string(forKey: "selectedVoice")
        return (loadedTTSService, loadedEngine, loadedLanguage, loadedVoice)
    }
    
    func validateSelectedOptions() -> Bool {
        print("available synt: \(availableSynthesizers). Selected: \(selectedTTSService)")
        print("available eng: \(availableEngines). Selected: \(selectedEngine)")
        print("available lang: \(availableLanguages). Selected: \(selectedLanguage)")
        print("available voice: \(availableVoices). Selected: \(selectedVoice)")
        return [
            availableSynthesizers.contains(selectedTTSService),
            availableEngines.contains(selectedEngine),
            availableLanguages.contains(selectedLanguage),
            availableVoices.contains(selectedVoice),
        ].allSatisfy { $0 == true }
    }
    
    static func defaultTTSSettings() -> (TTSService, String, String, String) {
        return (.local, "standard", "en-US", "com.apple.voice.compact.en-US.Samantha")
    }
}

