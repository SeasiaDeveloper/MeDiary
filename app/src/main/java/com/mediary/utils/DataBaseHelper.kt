package com.mediary.utils

import android.content.Context
import com.mediary.R
import com.mediary.database.entity.PromptQuestion

object DataBaseHelper {
    fun getQuestionsList(context:Context): List<PromptQuestion> {
        return object : ArrayList<PromptQuestion>() {
            init {
                add(PromptQuestion("What did I do for the first time today?", Utilities.setCurrentDate(), 1, context.getString(R.string.special_day)))
                add(PromptQuestion("What are the notable things which happened to me today?", Utilities.setCurrentDate(), 1,  context.getString(R.string.special_day)))
                add(PromptQuestion("What did I do related to today's holiday/anniversary?", Utilities.setCurrentDate(), 1,  context.getString(R.string.special_day)))
                add(PromptQuestion("If I received/sent a present today, what was it?", Utilities.setCurrentDate(), 1,  context.getString(R.string.special_day)))
                add(PromptQuestion("What can I do to make today more special?", Utilities.setCurrentDate(), 1,  context.getString(R.string.special_day)))
                add(PromptQuestion("Every Single day is unique, please choose a suitable word to describe today?", Utilities.setCurrentDate(), 1,  context.getString(R.string.special_day)))
                add(PromptQuestion("What sports did I do today to improve my health?", Utilities.setCurrentDate(), 2, context.getString(R.string.health)))
                add(PromptQuestion("Did I sleep well today? What did I dream of?", Utilities.setCurrentDate(), 2, context.getString(R.string.health)))
                add(PromptQuestion("What kind of foods did I eat for breakfast/lunch/dinner/snack/fruit?", Utilities.setCurrentDate(), 2, context.getString(R.string.health)))
                add(PromptQuestion("When did I get up/go to bed/muse today?", Utilities.setCurrentDate(), 2, context.getString(R.string.health)))
                add(PromptQuestion("Whose behavior or discourse made me feel good today?", Utilities.setCurrentDate(), 3, context.getString(R.string.social_interactions)))
                add(PromptQuestion("Whose behavior or discourse made me feel bad today?", Utilities.setCurrentDate(), 3,  context.getString(R.string.social_interactions)))
                add(PromptQuestion("Did I receive any surprise today? Who gave me surprise?", Utilities.setCurrentDate(), 3,  context.getString(R.string.social_interactions)))
                add(PromptQuestion("Who did/didn't I get along with today?", Utilities.setCurrentDate(), 3,  context.getString(R.string.social_interactions)))
                add(PromptQuestion("Who did I need to thank for their help today?", Utilities.setCurrentDate(), 3,  context.getString(R.string.social_interactions)))
                add(PromptQuestion("What did I learn from work today?", Utilities.setCurrentDate(), 4, context.getString(R.string.career_development)))
                add(PromptQuestion("What tasks did I do today?", Utilities.setCurrentDate(), 4, context.getString(R.string.career_development)))
                add(PromptQuestion("How were my work relationships today/Good/Bad?", Utilities.setCurrentDate(), 4, context.getString(R.string.career_development)))
                add(PromptQuestion("How were my achievements today?", Utilities.setCurrentDate(), 4, context.getString(R.string.career_development)))
                add(PromptQuestion("What problem did I encounter today? How did I solve it/them?", Utilities.setCurrentDate(), 4, context.getString(R.string.career_development)))
                add(PromptQuestion("How was my mental state today?", Utilities.setCurrentDate(), 5, context.getString(R.string.financial_independence)))
                add(PromptQuestion("How much did I earn today?", Utilities.setCurrentDate(), 5,  context.getString(R.string.financial_independence)))
                add(PromptQuestion("What did I buy today for me/family/friends?", Utilities.setCurrentDate(), 5,  context.getString(R.string.financial_independence)))
                add(PromptQuestion("What did I do today to achieve financial independence", Utilities.setCurrentDate(), 5,  context.getString(R.string.financial_independence)))
                add(PromptQuestion("What did I do for my family today?", Utilities.setCurrentDate(), 6, context.getString(R.string.family_happiness)))
                add(PromptQuestion("What have I done with my family today?", Utilities.setCurrentDate(), 6, context.getString(R.string.family_happiness)))
                add(PromptQuestion("What did my family do for me today?", Utilities.setCurrentDate(), 6, context.getString(R.string.family_happiness)))
                add(PromptQuestion("Did I let my family know I love them?", Utilities.setCurrentDate(), 6, context.getString(R.string.family_happiness)))
                add(PromptQuestion("Do I have any interesting notes about my pet/plant from today?", Utilities.setCurrentDate(), 6, context.getString(R.string.family_happiness)))
                add(PromptQuestion("Today, what actions must I take to move me closer to my goal(s)?", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
                add(PromptQuestion("What good habits did I practice today?", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
                add(PromptQuestion("What mistake did I make today?", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
                add(PromptQuestion("Three things that must get done tomorrow.", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
                add(PromptQuestion("What can I do today to make my future better?", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
                add(PromptQuestion("Do I still remember my life goal?", Utilities.setCurrentDate(), 7, context.getString(R.string.personal_development)))
            }
        }
    }

    fun getHintList(): List<String> {
        return object : ArrayList<String>() {
            init {
                add("Swipe down this view to see reading mode of current day diary.")
                add("First record the day's weather and mood.\n This will help you to remember. Swipe down to see reading mode.")
                add("Touch on the grid to get started writing.")
                add("Want to see some other day diary.Click on this.")
                add("Don't like some of the grid? No Problem! \n You are free to modify title or number of prompts.")
                add("List of all prompts available.You can also add custom prompts")
            }
        }
    }
 }