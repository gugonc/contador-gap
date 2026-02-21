package com.discordbotgap.contador_gap.bot;

import com.discordbotgap.contador_gap.GapRepository;
import com.discordbotgap.contador_gap.model.GapUser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GapListener extends ListenerAdapter {

    private final GapRepository repository;

    public GapListener(GapRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        
        if (event.getAuthor().isBot()) return;

        
        String rawMessage = event.getMessage().getContentRaw();
        String[] args = rawMessage.split("\\s+");
        String command = args[0].toLowerCase();

        // 🟢 !gap @usuario
        if (command.equals("!tomagap")) {
            
            List<Member> mentionedMembers = event.getMessage().getMentions().getMembers();

            if (mentionedMembers.isEmpty()) {
                event.getChannel().sendMessage("❌ **Como aplicar gap:** `!gap @Usuario`").queue();
                return;
            }

            
            Member targetMember = mentionedMembers.get(0);
            String userId = targetMember.getId();
            
            
            String userName = targetMember.getEffectiveName(); 

            
            GapUser userRecord = repository.findById(userId)
                    .orElse(new GapUser(userId, userName, 0));

            
            userRecord.setUsername(userName);
            userRecord.setTotalGaps(userRecord.getTotalGaps() + 1);
            repository.save(userRecord);

            
            Long totalGlobal = repository.sumAllGaps();
            List<GapUser> top3 = repository.findTop3ByOrderByTotalGapsDesc();

            
            StringBuilder resposta = new StringBuilder();
            resposta.append(" ⚠️ ").append(userName).append(" **Tomou GAP! ⚠️").append("!**\n");
            resposta.append("👤 Gaps de ").append(userName).append(": **").append(userRecord.getTotalGaps()).append("**\n");
            resposta.append("📊 Total de gaps tomados no servidor: **").append(totalGlobal != null ? totalGlobal : 1).append("**\n\n");
            
            resposta.append("🏆 **Rank de Gaps:**\n");
            for (int i = 0; i < top3.size(); i++) {
                GapUser u = top3.get(i);
                resposta.append("**").append(i + 1).append("º** - ").append(u.getUsername())
                        .append(" (").append(u.getTotalGaps()).append(" gaps)\n");
            }

            
            event.getChannel().sendMessage(resposta.toString()).queue();
        }

        // 🏆 !gap rank
        else if (command.equals("!gaprank")) {
            List<GapUser> topGappers = repository.findTop5ByOrderByTotalGapsDesc();

            if (topGappers.isEmpty()) {
                event.getChannel().sendMessage("📉 Ainda não há gaps registados!").queue();
                return;
            }

            StringBuilder ranking = new StringBuilder("🏆 **Top 5 do Servidor:**\n\n");
            for (int i = 0; i < topGappers.size(); i++) {
                GapUser u = topGappers.get(i);
                ranking.append("**").append(i + 1).append("º** - ").append(u.getUsername())
                       .append(" (").append(u.getTotalGaps()).append(" gaps)\n");
            }

            event.getChannel().sendMessage(ranking.toString()).queue();
        }
    }
}