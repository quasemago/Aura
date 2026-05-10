import { Settings } from "@/infrastructure/config/settings";
import { type ChatInputCommandInteraction, EmbedBuilder, MessageFlags } from "discord.js";
import { Service } from "typedi";
import { AbstractBaseUseCase } from "../base-usecase";

@Service({ transient: true })
export class AboutCommandUseCase extends AbstractBaseUseCase<ChatInputCommandInteraction, void> {
  constructor(private readonly settings: Settings) {
    super();
  }

  public async execute(input: ChatInputCommandInteraction): Promise<void> {
    const embed = new EmbedBuilder()
      .setTitle(`🤖 ${input.client.user.displayName}`)
      .setDescription(
        "Aura is a Discord bot that provides a variety of features to enhance your server."
      )
      .setColor(0x0099ff)
      .setThumbnail(input.client.user.displayAvatarURL())
      .setTimestamp()
      .addFields(
        {
          name: "Created by",
          value: `quasemago [(Github)](https://github.com/quasemago)`,
          inline: true
        },
        { name: "Version", value: this.settings.getBotVersion(), inline: true },
        { name: "Uptime", value: "uptime", inline: true }
      )
      .setFooter({
        text: `Requested by ${input.user.username}`,
        iconURL: input.user.displayAvatarURL()
      });

    await input.reply({
      embeds: [embed],
      flags: MessageFlags.Ephemeral
    });
  }
}
