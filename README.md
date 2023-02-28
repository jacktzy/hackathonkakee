# Hackathon Kakee

> A hackathon promoting platform mobile application.

![picture](https://github.com/jacktzy/hackathonkakee/blob/master/app/src/debug/res/mipmap-xxxhdpi/app_icon.png)

## Introduction

&nbsp;&nbsp;&nbsp;&nbsp;Nowadays, there are rising number of hackathons have been held in Malaysia. Many sponsors are willing to cooperate with the hackathons organizers to discover talented individuals among universities students. However, it is difficult for hackathon organizers to promote their event among universities students. Some of the organizers need to rely on other social media apps to promote their hackathons. Besides, participants are also difficult to discover hackathon events efficiently. Although they can find hackathons on certain social media platforms, it is also quite hard to find teammates if they are one of the solo participants.<br />
&nbsp;&nbsp;&nbsp;&nbsp;A platform is required, in which hackathon organizer can promote their events and participants can discover all happening hackathons in Malaysia easily. Therefore, ***Hackathon Kakee***, a hackathon promoting mobile application is produced which can solve the problems faced by hackathon organizers and universities students. Hackathon organizers can easily promote their events, news and annoucements in this mobile application, while universities students can discover all hackathon happens in Malaysia easily, find hackathon teammates and join hackathon by one click only.

## Technical Stack

<table>
<tr>
<td><img src="https://source.android.com/static/docs/setup/images/Android_symbol_green_RGB.png" align="center" width="100" ></td>
<td>Android App Development </td>
</tr>
<tr>
<td><img src="https://www.gcreddy.com/wp-content/uploads/2021/05/Java-Programming-Language.png" align="center" width="100" ></td>
<td>Java</td>
</tr>
<tr>
<td><img src="https://upload.wikimedia.org/wikipedia/commons/b/bd/Firebase_Logo.png" align="center" width="120" ></td>
<td>Firebase</td>
</tr>
<tr>
<td><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Google_Material_Design_Logo.svg/1024px-Google_Material_Design_Logo.svg.png" align="center" width="65" ></td>
<td>Material Design 3</td>
</tr>
</table>

## Functionalities

1. General Module
   - Login
     - User can login as organizers or participants.
   - Sign up
     - New user can sign up as new organizer or new participant.
   - Reset password
     - Participants or organizers can reset their password if they forget password.
   - Logout
     - Participants and organizers can logout from their accounts.
2. Organizer Module
   - Manage hackathon
     - Organizers can create new hackathon, update hackathon details, and delete hackathons.
   - Manage hackathon announcement
     - Organizers can create, edit, and delete hackathon announcements which include hackathon instruction to be announced to that hackathon participants only
   - Manage participants
     - Organizers can view participants details and delete participants.
3. Participants profile module
   - Edit profile
     - Participants can update their profile details.
   - Manage resume
     - Participants can upload and delete their resume in pdf file.
4. Hackathon module
   - View hackathon
     - Participants can view hackathon details.
   - Search hackathon
     - Participants can search for hackathon by entering hackathon name.
   - Hackathon registration
     - Participants can register for hackathon. Participants also can withdraw from the hackathon they already joined.
5. Participants team formation module
   - View team list
     - Participants can view all public teams.
   - Manage team
     - Participants can create new team (in that hackathon only), either public team or private team. Team leader can delete their team. The team leader can update the team information such as edit team name, team description, and delete team members. Team leader can invite other participants to join his team by sharing the team ID as invitation code.
   - Join team
     - Participants can join team that created by others by entering team ID. They also can quit from their team.
 
## Extra Functionalities

- View team ranking
  - Participant can view team final ranking in the end of the hackathon
- Edit team ranking
  - Organizer can edit the team’s final ranking in the end of the hackathon. After organizer updating final ranking, the team ranking in the participant ranking page will be updated immediately, and the participants in the top 5 teams will get points for reward claiming.
- Manage team (Organizer)
  - Organizer can view all the teams in that hackathon. Organizer also can delete the team.
- Hall of fame
  - Hall of fame is the ranking based on participants points which the points can be get when participants win Top 5 in the hackathon. Participants can view their ranking in this page.
- Point reward system
  - There is a page where participants can claim their reward based on their points collected. Each reward can only be claimed once only.
- Email sending
  - A confirmation email will be sent to participants when they claim reward in claim reward pages.
An email verification email also will be sent to participants when they create a new account.
An reset password email also will be sent to participants when they reset their password.
- Sort order of hackathon
  - Participants can sort the order of hackathon list in all hackathon page. The sorting can be based on hackathon date, hackathon name and hackathon mode.
- Organizer profile
  - Organizer can view their profile. Besides, organizer also can edit their profile details and update their profile picture.
- Manage News
  - Organizer can manage hackathon news such as adding, editing and deleting a news.
- Hackathon sharing
  - Participants can share hackathon to social media such as WhatsApp, Facebook, Instagram, email etc. to invite their friends to participate in the hackathon.
