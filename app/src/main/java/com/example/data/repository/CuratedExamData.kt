package com.example.data.repository

import com.example.data.model.*

object CuratedExamData {

    val books = listOf(
        Book(
            id = "part_a_polity",
            titleEn = "Indian Constitution & Polity",
            titleHi = "भारतीय संविधान और राजव्यवस्था",
            descriptionEn = "Detailed study of Constitution, Parliament, Executive, and state machinery for RPSC.",
            descriptionHi = "RPSC के लिए संविधान, संसद, कार्यपालिका और राज्य तंत्र का विस्तृत अध्ययन।",
            part = "Part A",
            iconName = "balance"
        ),
        Book(
            id = "part_a_history",
            titleEn = "History, Art & Culture of Rajasthan",
            titleHi = "राजस्थान का इतिहास, कला और संस्कृति",
            descriptionEn = "Major historical landmarks, freedom struggle, folk deities, fairs, and festivals of Rajasthan.",
            descriptionHi = "राजस्थान के प्रमुख ऐतिहासिक स्थल, स्वतंत्रता संग्राम, लोक देवता, मेले और त्योहार।",
            part = "Part A",
            iconName = "museum"
        ),
        Book(
            id = "part_b_municipal_act",
            titleEn = "Rajasthan Municipalities Act, 2009",
            titleHi = "राजस्थान नगरपालिका अधिनियम, 2009",
            descriptionEn = "Core sections (3 to 327) on Constitution, Revenue, Property, and Conduct of Business.",
            descriptionHi = "संविधान, राजस्व, संपत्ति और कार्य संचालन पर मूल धाराएं (3 से 327)।",
            part = "Part B",
            iconName = "domain"
        ),
        Book(
            id = "part_b_schemes",
            titleEn = "Welfare Schemes & Rules",
            titleHi = "कल्याणकारी योजनाएं और नियम",
            descriptionEn = "Urban welfare schemes including Indira Rasoi, Swachh Bharat Mission (U), AMRUT, and Rules 1974.",
            descriptionHi = "शहरी कल्याणकारी योजनाएं जिनमें इंदिरा रसोई, स्वच्छ भारत मिशन (शहरी), अमरुत और नियम 1974 शामिल हैं।",
            part = "Part B",
            iconName = "verified"
        )
    )

    val chapters = listOf(
        Chapter(
            id = "ch_polity_1",
            bookId = "part_a_polity",
            titleEn = "Salient Features of the Indian Constitution",
            titleHi = "भारतीय संविधान की प्रमुख विशेषताएं",
            estimatedReadingTime = "12 mins",
            orderIndex = 1,
            subtopicsEn = listOf("Preamble", "Sources of Constitution", "Sovereignty & Republic", "Federal vs Unitary Structure"),
            subtopicsHi = listOf("प्रस्तावना", "संविधान के स्रोत", "संप्रभुता और गणतंत्र", "संघीय बनाम एकात्मक संरचना")
        ),
        Chapter(
            id = "ch_polity_2",
            bookId = "part_a_polity",
            titleEn = "Fundamental Rights & Duties",
            titleHi = "मौलिक अधिकार और कर्तव्य",
            estimatedReadingTime = "15 mins",
            orderIndex = 2,
            subtopicsEn = listOf("Articles 12 to 35", "Right to Equality", "Right to Freedom", "Fundamental Duties (Part IV-A)"),
            subtopicsHi = listOf("अनुच्छेद 12 से 35", "समानता का अधिकार", "स्वतंत्रता का अधिकार", "मौलिक कर्तव्य (भाग IV-क)")
        ),
        Chapter(
            id = "ch_history_1",
            bookId = "part_a_history",
            titleEn = "Major Dynasties & Landmarks of Rajasthan",
            titleHi = "राजस्थान के प्रमुख राजवंश और स्थल",
            estimatedReadingTime = "18 mins",
            orderIndex = 1,
            subtopicsEn = listOf("Mewar Dynasty", "Marwar Dynasty", "Forts of Chittorgarh & Mehrangarh", "Archeological Sites"),
            subtopicsHi = listOf("मेवाड़ राजवंश", "मारवाड़ राजवंश", "चित्तौड़गढ़ और मेहरानगढ़ के किले", "पुरातात्विक स्थल")
        ),
        Chapter(
            id = "ch_history_2",
            bookId = "part_a_history",
            titleEn = "Folk Arts, Paintings and Festivals",
            titleHi = "लोक कलाएं, चित्रकला और त्योहार",
            estimatedReadingTime = "14 mins",
            orderIndex = 2,
            subtopicsEn = listOf("Thewas Art", "Phad Paintings", "Pushkar Fair", "Ghoomar Folk Dance"),
            subtopicsHi = listOf("थेवा कला", "फड़ चित्रकला", "पुष्कर मेला", "घूमर लोक नृत्य")
        ),
        Chapter(
            id = "ch_municipal_1",
            bookId = "part_b_municipal_act",
            titleEn = "Constitution & Government of Municipalities (Sec 3-50)",
            titleHi = "नगरपालिकाओं का गठन और शासन (धारा 3-50)",
            estimatedReadingTime = "20 mins",
            orderIndex = 1,
            subtopicsEn = listOf("Section 3: Delimitation", "Section 5: Establishment", "Section 24: Disqualifications of members", "Section 45: Core civic functions"),
            subtopicsHi = listOf("धारा 3: परिसीमन", "धारा 5: स्थापना", "धारा 24: सदस्यों की अयोग्यताएं", "धारा 45: मुख्य नगरपालिका कृत्य")
        ),
        Chapter(
            id = "ch_municipal_2",
            bookId = "part_b_municipal_act",
            titleEn = "Municipal Property and Revenue (Sec 67-100)",
            titleHi = "नगरपालिका संपत्ति और राजस्व (धारा 67-100)",
            estimatedReadingTime = "16 mins",
            orderIndex = 2,
            subtopicsEn = listOf("Section 67: Acquisition", "Section 76: State Finance Commission", "Section 101: Taxes leviable"),
            subtopicsHi = listOf("धारा 67: संपत्ति अर्जन", "धारा 76: राज्य वित्त आयोग", "धारा 101: प्रभार्य कर")
        ),
        Chapter(
            id = "ch_schemes_1",
            bookId = "part_b_schemes",
            titleEn = "Indira Gandhi Urban Employment Guarantee Scheme (IRGY-U)",
            titleHi = "इंदिरा गांधी शहरी रोजगार गारंटी योजना (IRGY-U)",
            estimatedReadingTime = "10 mins",
            orderIndex = 1,
            subtopicsEn = listOf("Aim & Eligibility", "Guaranteed Work Days (125 days)", "Wages & Executing Agencies", "Social Audit"),
            subtopicsHi = listOf("उद्देश्य और पात्रता", "गारंटीकृत कार्य दिवस (125 दिन)", "मजदूरी और निष्पादन एजेंसियां", "सामाजिक अंकेक्षण")
        ),
        Chapter(
            id = "ch_schemes_2",
            bookId = "part_b_schemes",
            titleEn = "Swachh Bharat Mission (U) 2.0 & AMRUT",
            titleHi = "स्वच्छ भारत मिशन (शहरी) 2.0 और अमरूत (AMRUT)",
            estimatedReadingTime = "12 mins",
            orderIndex = 2,
            subtopicsEn = listOf("SBM-U 2.0 Garbage Free Cities", "AMRUT 2.0 Water Security", "Funding Patterns (Central/State)"),
            subtopicsHi = listOf("SBM-U 2.0 कचरा मुक्त शहर", "अमृत 2.0 जल सुरक्षा", "वित्तपोषण पैटर्न (केंद्र/राज्य)")
        )
    )

    val chapterContents = mapOf(
        "ch_polity_1" to ChapterContent(
            chapterId = "ch_polity_1",
            pagesEn = listOf(
                "The Indian Constitution is the longest written constitution of any sovereign country in the world. At its adoption, it had 395 Articles in 22 Parts and 8 Schedules. Currently, it has about 470 Articles in 25 Parts and 12 Schedules.\n\nKey features include: Draw from several sources (Structural part from GoI Act 1935, Philosophical from US and Irish, Political from British Constitution). It is a unique blend of rigidity and flexibility.",
                "The Preamble is the introduction to the Constitution and outlines its objectives. Key words:\n- SOVEREIGN: Independent authority, not subject to external control.\n- SOCIALIST: Democratic Socialism aiming to end poverty, ignorance, and inequality.\n- SECULAR: All religions have equal support and protection.\n- DEMOCRATIC: Government derives authority from the will of the people.\n- REPUBLIC: Head of the state is elected, not hereditary."
            ),
            pagesHi = listOf(
                "भारतीय संविधान दुनिया के किसी भी संप्रभु देश का सबसे लंबा लिखित संविधान है। इसे अपनाते समय इसमें 22 भागों और 8 अनुसूचियों में 395 अनुच्छेद थे। वर्तमान में, इसमें 25 भागों और 12 अनुसूचियों में लगभग 470 अनुच्छेद हैं।\n\nप्रमुख विशेषताओं में शामिल हैं: कई स्रोतों से प्रेरणा (भारत सरकार अधिनियम 1935 से संरचनात्मक भाग, अमेरिकी और आयरिश से दार्शनिक भाग, ब्रिटिश संविधान से राजनीतिक भाग)। यह लचीलेपन और कठोरता का एक अनूठा मिश्रण है।",
                "प्रस्तावना संविधान का परिचय है और इसके उद्देश्यों को रेखांकित करती है। प्रमुख शब्द:\n- संप्रभु: स्वतंत्र सत्ता, बाहरी नियंत्रण के अधीन नहीं।\n- समाजवादी: लोकतांत्रिक समाजवाद जिसका उद्देश्य गरीबी, अज्ञानता और असमानता को समाप्त करना है।\n- पंथनिरपेक्ष: सभी धर्मों को समान समर्थन और संरक्षण प्राप्त है।\n- लोकतांत्रिक: सरकार अपनी सत्ता जनता की इच्छा से प्राप्त करती है।\n- गणराज्य: राज्य का प्रमुख निर्वाचित होता है, वंशानुगत नहीं।"
            )
        ),
        "ch_polity_2" to ChapterContent(
            chapterId = "ch_polity_2",
            pagesEn = listOf(
                "Fundamental Rights are enshrined in Part III of the Constitution from Article 12 to 35. These are called the Magna Carta of India. They are justiciable, allowing individuals to move the Supreme Court (Art 32) directly for enforcement.\n\nOriginally 7 rights were granted, but the Right to Property (Art 31) was deleted by the 44th Amendment in 1978 and made a legal right under Art 300-A.",
                "The six fundamental rights are:\n1. Right to Equality (Articles 14-18)\n2. Right to Freedom (Articles 19-22)\n3. Right against Exploitation (Articles 23-24)\n4. Right to Freedom of Religion (Articles 25-28)\n5. Cultural and Educational Rights (Articles 29-30)\n6. Right to Constitutional Remedies (Article 32)\n\nFundamental Duties were added by the 42nd Amendment in 1976 on the recommendation of the Swaran Singh Committee. Article 51A in Part IVA lists 11 fundamental duties."
            ),
            pagesHi = listOf(
                "मौलिक अधिकार संविधान के भाग III में अनुच्छेद 12 से 35 तक निहित हैं। इन्हें भारत का मैग्ना कार्टा कहा जाता है। ये न्यायसंगत हैं, जो व्यक्तियों को लागू करने के लिए सीधे सुप्रीम कोर्ट (अनुच्छेद 32) जाने की अनुमति देते हैं।\n\nमूल रूप से 7 अधिकार दिए गए थे, लेकिन संपत्ति के अधिकार (अनुच्छेद 31) को 1978 में 44वें संशोधन द्वारा हटा दिया गया और अनुच्छेद 300-क के तहत कानूनी अधिकार बना दिया गया।",
                "छह मौलिक अधिकार हैं:\n1. समानता का अधिकार (अनुच्छेद 14-18)\n2. स्वतंत्रता का अधिकार (अनुच्छेद 19-22)\n3. शोषण के खिलाफ अधिकार (अनुच्छेद 23-24)\n4. धार्मिक स्वतंत्रता का अधिकार (अनुच्छेद 25-28)\n5. संस्कृति और शिक्षा का अधिकार (अनुच्छेद 29-30)\n6. संवैधानिक उपचारों का अधिकार (अनुच्छेद 32)\n\nस्वर्ण सिंह समिति की सिफारिश पर 1976 में 42वें संशोधन द्वारा मौलिक कर्तव्यों को जोड़ा गया था। भाग IVक में अनुच्छेद 51क के तहत 11 मौलिक कर्तव्य सूचीबद्ध हैं।"
            )
        ),
        "ch_municipal_1" to ChapterContent(
            chapterId = "ch_municipal_1",
            pagesEn = listOf(
                "The Rajasthan Municipalities Act, 2009 extends to the whole state of Rajasthan excluding Cantonment areas. Under Section 3, the State Government has the power to declare any local area to be a municipality, or delimit/exclude areas from it.\n\nSection 5 details the establishment of:\n1. Municipal Board (Nagar Palika Board) for a transitional area.\n2. Municipal Council (Nagar Parishad) for a smaller urban area.\n3. Municipal Corporation (Nagar Nigam) for a larger urban area.",
                "Section 24 specifies disqualifications for members: bankruptcy, unsounded mind, having more than 2 children (subject to limits), or being convicted of offenses.\nSection 45 describes core civic functions (obligatory tasks) such as sanitation, waste management, registry of births and deaths, naming of streets, and lighting of public roads."
            ),
            pagesHi = listOf(
                "राजस्थान नगरपालिका अधिनियम, 2009 छावनी क्षेत्रों को छोड़कर पूरे राजस्थान राज्य में लागू है। धारा 3 के तहत, राज्य सरकार को किसी भी स्थानीय क्षेत्र को नगरपालिका घोषित करने, या उससे क्षेत्रों को अलग करने/शामिल करने की शक्ति है।\n\nधारा 5 के तहत स्थापना का विवरण है:\n1. संक्रमणकालीन क्षेत्र के लिए नगरपालिका बोर्ड (नगर पालिका बोर्ड)।\n2. लघुतर शहरी क्षेत्र के लिए नगर परिषद।\n3. वृहत्तर शहरी क्षेत्र के लिए नगर निगम।",
                "धारा 24 सदस्यों के लिए अयोग्यताएं निर्दिष्ट करती है: दिवालियापन, विकृत मस्तिष्क, 2 से अधिक बच्चे होना (सीमाओं के अधीन), या अपराधों के लिए दोषी ठहराया जाना।\nधारा 45 मुख्य नगरपालिका कृत्यों (अनिवार्य कार्यों) का वर्णन करती है जैसे स्वच्छता, कचरा प्रबंधन, जन्म और मृत्यु का पंजीकरण, सड़कों का नामकरण और सार्वजनिक सड़कों पर प्रकाश व्यवस्था।"
            )
        ),
        "ch_municipal_2" to ChapterContent(
            chapterId = "ch_municipal_2",
            pagesEn = listOf(
                "Section 67 outlines how Municipalities can acquire and hold property, both moveable and immoveable. Under Section 76, the State Finance Commission makes recommendations regarding tax distribution between State and Municipalities, grant-in-aids, and measures to improve financial positions.",
                "Section 101 specifies taxes leviable by Municipalities, which include tax on lands and buildings, tax on professions/trades, tax on advertisements, and Toll on roads/bridges. Under Section 102, they can also levy non-obligatory discretionary taxes like tax on vehicles, dogs, and boats."
            ),
            pagesHi = listOf(
                "धारा 67 रेखांकित करती है कि नगरपालिकाएं किस प्रकार चल और अचल संपत्ति अर्जित और धारण कर सकती हैं। धारा 76 के तहत, राज्य वित्त आयोग राज्य और नगरपालिकाओं के बीच करों के वितरण, सहायता अनुदान और वित्तीय स्थिति में सुधार के उपायों के संबंध में सिफारिशें करता है।",
                "धारा 101 नगरपालिकाओं द्वारा वसूले जाने वाले करों को निर्दिष्ट करती है, जिसमें भूमि और भवनों पर कर, व्यवसायों/व्यापारों पर कर, विज्ञापनों पर कर और सड़कों/पुलों पर टोल शामिल हैं। धारा 102 के तहत, वे विवेकाधीन कर भी लगा सकते हैं जैसे वाहनों, कुत्तों और नावों पर कर।"
            )
        ),
        "ch_schemes_1" to ChapterContent(
            chapterId = "ch_schemes_1",
            pagesEn = listOf(
                "The Indira Gandhi Urban Employment Guarantee Scheme (IRGY-U) was launched on September 9, 2022. It is modeled on MGNREGA to provide employment security in urban areas of Rajasthan.\n\nKey features:\n- Eligibility: Citizens aged 18 to 60 years living in municipal areas.\n- Benefits: 125 days of guaranteed wage employment per family per year.\n- Job Card: Families register with Jan Aadhaar card to receive a job card within 15 days.",
                "Types of works permitted:\n1. Environment protection (tree plantation, park maintenance).\n2. Water conservation (cleaning stepwells, ponds).\n3. Sanitation and waste management.\n4. Heritage conservation.\n\nWages are credited directly to Jan Aadhaar-linked bank accounts within 15 days of work completion."
            ),
            pagesHi = listOf(
                "इंदिरा गांधी शहरी रोजगार गारंटी योजना (IRGY-U) का शुभारंभ 9 सितंबर, 2022 को किया गया था। यह राजस्थान के शहरी क्षेत्रों में रोजगार सुरक्षा प्रदान करने के लिए मनरेगा (MGNREGA) की तर्ज पर तैयार की गई है।\n\nमुख्य विशेषताएं:\n- पात्रता: नगरपालिका क्षेत्रों में रहने वाले 18 से 60 वर्ष की आयु के नागरिक।\n- लाभ: प्रति परिवार प्रति वर्ष 125 दिनों का गारंटीकृत रोजगार।\n- जॉब कार्ड: परिवार जन आधार कार्ड के माध्यम से पंजीकरण करते हैं और 15 दिनों के भीतर जॉब कार्ड प्राप्त करते हैं।",
                "अनुमत कार्यों के प्रकार:\n1. पर्यावरण संरक्षण (वृक्षारोपण, उद्यानों का रखरखाव)।\n2. जल संरक्षण (बावडियों, तालाबों की सफाई)।\n3. स्वच्छता और कचरा प्रबंधन।\n4. विरासत संरक्षण।\n\nकार्य पूरा होने के 15 दिनों के भीतर मजदूरी सीधे जन आधार से जुड़े बैंक खातों में जमा की जाती है।"
            )
        ),
        "ch_schemes_2" to ChapterContent(
            chapterId = "ch_schemes_2",
            pagesEn = listOf(
                "Swachh Bharat Mission (Urban) 2.0 was launched on October 1, 2021, with a vision of achieving 'Garbage Free' status for all cities. Key focal areas include complete wastewater treatment, source segregation of waste, reducing single-use plastics, and remediation of legacy dumpsites.",
                "Atal Mission for Rejuvenation and Urban Transformation (AMRUT) 2.0 was also launched on October 1, 2021. It aims to make cities water-secure and provide 100% water tap connections to all households in around 4,700 urban local bodies.\n\nFunding is shared between Centre and State depending on city populations (e.g., 50:50 for cities above 10 Lakhs, 60:40 or 90:10 for smaller/Himalayan cities)."
            ),
            pagesHi = listOf(
                "स्वच्छ भारत मिशन (शहरी) 2.0 का शुभारंभ 1 अक्टूबर, 2021 को सभी शहरों को 'कचरा मुक्त' बनाने के दृष्टिकोण से किया गया था। प्रमुख क्षेत्रों में अपशिष्ट जल का पूर्ण उपचार, कचरे का स्रोत पर पृथक्करण, एकल-उपयोग वाले प्लास्टिक को कम करना और पुराने कचरे के ढेरों का उपचार शामिल हैं।",
                "अटल नवीनीकरण और शहरी परिवर्तन मिशन (AMRUT) 2.0 का शुभारंभ भी 1 अक्टूबर, 2021 को किया गया था। इसका उद्देश्य शहरों को जल-सुरक्षित बनाना और लगभग 4,700 शहरी स्थानीय निकायों में सभी घरों को 100% पानी के नल कनेक्शन प्रदान करना है।\n\nवित्तपोषण केंद्र और राज्य के बीच शहर की आबादी के आधार पर साझा किया जाता है (जैसे, 10 लाख से अधिक आबादी वाले शहरों के लिए 50:50, छोटे/हिमालयी शहरों के लिए 60:40 या 90:10)।"
            )
        )
    )

    val questions = listOf(
        Question(
            id = "q_1",
            testId = "test_polity_1",
            textEn = "Which of the following is not a basic feature of the Constitution of India?",
            textHi = "निम्नलिखित में से कौन-सी भारतीय संविधान की मूल विशेषता नहीं है?",
            optionsEn = listOf("Sovereignty of Parliament", "Judicial Review", "Federalism", "Fundamental Rights"),
            optionsHi = listOf("संसद की संप्रभुता", "न्यायिक समीक्षा", "संघवाद", "मौलिक अधिकार"),
            correctAnswerIndex = 0,
            type = QuestionType.SINGLE_CHOICE,
            explanationEn = "While the British system has Sovereignty of Parliament, India features constitutional supremacy. Parliament can amend but cannot violate the basic structure.",
            explanationHi = "ब्रिटिश व्यवस्था में संसद की संप्रभुता है, जबकि भारत में संवैधानिक सर्वोच्चता है। संसद संशोधन कर सकती है लेकिन मूल ढांचे का उल्लंघन नहीं कर सकती।"
        ),
        Question(
            id = "q_2",
            testId = "test_polity_1",
            textEn = "Assertion (A): The Constitution of India is a rigid one.\nReason (R): It can be amended only through a special majority in Parliament.",
            textHi = "कथन (A): भारत का संविधान एक कठोर संविधान है।\nकारण (R): इसे केवल संसद में विशेष बहुमत के माध्यम से ही संशोधित किया जा सकता है।",
            optionsEn = listOf(
                "Both A and R are true and R is the correct explanation of A",
                "Both A and R are true but R is not the correct explanation of A",
                "A is true but R is false",
                "A is false but R is true"
            ),
            optionsHi = listOf(
                "A और R दोनों सत्य हैं और R, A की सही व्याख्या है",
                "A और R दोनों सत्य हैं लेकिन R, A की सही व्याख्या नहीं है",
                "A सत्य है लेकिन R असत्य है",
                "A असत्य है लेकिन R सत्य है"
            ),
            correctAnswerIndex = 3,
            type = QuestionType.ASSERTION_REASON,
            explanationEn = "Assertion A is false because the Indian Constitution is a blend of rigidity and flexibility, and some parts can be amended by a simple majority. Reason R is true as major parts require a special majority.",
            explanationHi = "कथन A असत्य है क्योंकि भारतीय संविधान लचीलेपन और कठोरता का मिश्रण है, और कुछ भागों को साधारण बहुमत से संशोधित किया जा सकता है। कारण R सत्य है क्योंकि प्रमुख भागों के लिए विशेष बहुमत की आवश्यकता होती है।"
        ),
        Question(
            id = "q_3",
            testId = "test_municipal_1",
            textEn = "As per Section 5 of the Rajasthan Municipalities Act, 2009, a 'Municipal Council' is established for which type of area?",
            textHi = "राजस्थान नगरपालिका अधिनियम, 2009 की धारा 5 के अनुसार, किस प्रकार के क्षेत्र के लिए 'नगर परिषद' की स्थापना की जाती है?",
            optionsEn = listOf("Transitional Area", "Smaller Urban Area", "Larger Urban Area", "Cantonment Area"),
            optionsHi = listOf("संक्रमणकालीन क्षेत्र", "लघुतर शहरी क्षेत्र", "वृहत्तर शहरी क्षेत्र", "छावनी क्षेत्र"),
            correctAnswerIndex = 1,
            type = QuestionType.SINGLE_CHOICE,
            explanationEn = "Under Section 5: Transitional area has a Municipal Board, smaller urban area has a Municipal Council, and larger urban area has a Municipal Corporation.",
            explanationHi = "धारा 5 के तहत: संक्रमणकालीन क्षेत्र में नगरपालिका बोर्ड होता है, लघुतर शहरी क्षेत्र में नगर परिषद होती है, और वृहत्तर शहरी क्षेत्र में नगर निगम होता है।"
        ),
        Question(
            id = "q_4",
            testId = "test_municipal_1",
            textEn = "Under Section 24 of the Act, which of the following are grounds for disqualification of a municipality member?",
            textHi = "अधिनियम की धारा 24 के तहत, निम्नलिखित में से कौन से नगरपालिका सदस्य की अयोग्यता के आधार हैं?",
            optionsEn = listOf(
                "Having more than two children after the prescribed date",
                "Conviction under Food Adulteration Act",
                "Holds an office of profit in the Municipality",
                "All of the above"
            ),
            optionsHi = listOf(
                "निर्धारित तिथि के बाद दो से अधिक बच्चे होना",
                "खाद्य अपमिश्रण निवारण अधिनियम के तहत दोषसिद्धि",
                "नगरपालिका में लाभ का पद धारण करना",
                "उपरोक्त सभी"
            ),
            correctAnswerIndex = 3,
            type = QuestionType.SINGLE_CHOICE,
            explanationEn = "All listed conditions are valid grounds of disqualification under Section 24 of the Rajasthan Municipalities Act, 2009.",
            explanationHi = "राजस्थान नगरपालिका अधिनियम, 2009 की धारा 24 के तहत सूचीबद्ध सभी शर्तें अयोग्यता के वैध आधार हैं।"
        ),
        Question(
            id = "q_5",
            testId = "test_schemes_1",
            textEn = "Consider the following statements regarding the Indira Gandhi Urban Employment Guarantee Scheme (IRGY-U):\n1. It guarantees 125 days of employment in urban areas.\n2. The eligible age group is 18 to 60 years.",
            textHi = "इंदिरा गांधी शहरी रोजगार गारंटी योजना (IRGY-U) के संबंध में निम्नलिखित कथनों पर विचार करें:\n1. यह शहरी क्षेत्रों में 125 दिनों के रोजगार की गारंटी देती है।\n2. पात्र आयु वर्ग 18 से 60 वर्ष है।",
            optionsEn = listOf("Only 1 is correct", "Only 2 is correct", "Both 1 and 2 are correct", "Neither 1 nor 2 is correct"),
            optionsHi = listOf("केवल 1 सही है", "केवल 2 सही है", "1 और 2 दोनों सही हैं", "न तो 1 और न ही 2 सही है"),
            correctAnswerIndex = 2,
            type = QuestionType.STATEMENT_BASED,
            explanationEn = "Both statements are correct. The scheme initially provided 100 days, but was upgraded to 125 days. Age eligibility is 18-60 years.",
            explanationHi = "दोनों कथन सही हैं। योजना में प्रारंभ में 100 दिन का रोजगार मिलता था, जिसे बढ़ाकर 125 दिन कर दिया गया है। आयु पात्रता 18-60 वर्ष है।"
        ),
        Question(
            id = "q_6",
            testId = "test_schemes_1",
            textEn = "What is the primary objective of the Swachh Bharat Mission (Urban) 2.0?",
            textHi = "स्वच्छ भारत मिशन (शहरी) 2.0 का प्राथमिक उद्देश्य क्या है?",
            optionsEn = listOf("Garbage Free Cities", "100% Tap Water to every home", "Constructing new houses", "Livelihood training"),
            optionsHi = listOf("कचरा मुक्त शहर", "हर घर में 100% नल का पानी", "नए घरों का निर्माण", "आजीविका प्रशिक्षण"),
            correctAnswerIndex = 0,
            type = QuestionType.SINGLE_CHOICE,
            explanationEn = "Swachh Bharat Mission (U) 2.0 targets 'Garbage Free' cities. AMRUT 2.0 targets water security and 100% tap water supply.",
            explanationHi = "स्वच्छ भारत मिशन (शहरी) 2.0 का लक्ष्य 'कचरा मुक्त' शहर है। अमरूत (AMRUT) 2.0 का लक्ष्य जल सुरक्षा और 100% नल का पानी पहुंचाना है।"
        )
    )

    val flashcards = listOf(
        FlashcardEntity(
            id = "fc_1",
            frontEn = "Which part of the Indian Constitution is called the Magna Carta?",
            frontHi = "भारतीय संविधान के किस भाग को मैग्ना कार्टा कहा जाता है?",
            backEn = "Part III (Fundamental Rights)",
            backHi = "भाग III (मौलिक अधिकार)",
            category = "Constitution",
            isDaily = true,
            isFavorite = false
        ),
        FlashcardEntity(
            id = "fc_2",
            frontEn = "How many days of employment are guaranteed under IRGY-U in Rajasthan?",
            frontHi = "राजस्थान में IRGY-U के तहत कितने दिनों के रोजगार की गारंटी है?",
            backEn = "125 Days",
            backHi = "125 दिन",
            category = "Schemes",
            isDaily = true,
            isFavorite = false
        ),
        FlashcardEntity(
            id = "fc_3",
            frontEn = "Under which Section of the 2009 Act is the Delimitation of Municipalities defined?",
            frontHi = "2009 के अधिनियम की किस धारा के तहत नगरपालिकाओं का परिसीमन परिभाषित है?",
            backEn = "Section 3",
            backHi = "धारा 3",
            category = "Municipal Act",
            isDaily = false,
            isFavorite = true
        ),
        FlashcardEntity(
            id = "fc_4",
            frontEn = "Which fort in Rajasthan is known for having 9 identical palaces built by Madho Singh II?",
            frontHi = "राजस्थान का कौन सा किला माधो सिंह द्वितीय द्वारा निर्मित 9 समान महलों के लिए जाना जाता है?",
            backEn = "Nahargarh Fort, Jaipur",
            backHi = "नाहरगढ़ किला, जयपुर",
            category = "Rajasthan GK",
            isDaily = false,
            isFavorite = false
        ),
        FlashcardEntity(
            id = "fc_5",
            frontEn = "Who recommends the distribution of tax revenues between the State and Municipalities?",
            frontHi = "राज्य और नगरपालिकाओं के बीच कर राजस्व के वितरण की सिफारिश कौन करता है?",
            backEn = "State Finance Commission (under Section 76)",
            backHi = "राज्य वित्त आयोग (धारा 76 के तहत)",
            category = "Municipal Act",
            isDaily = true,
            isFavorite = false
        )
    )
}
