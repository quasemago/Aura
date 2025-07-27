import { CommandInteraction, EmbedBuilder, MessageFlags } from "discord.js";
import { injectable } from "inversify";
import { AbstractBaseUseCase } from "./base-usecase";

@injectable()
export class AboutCommandUseCase extends AbstractBaseUseCase<CommandInteraction, void> {
  public async execute(input: CommandInteraction): Promise<void> {
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
        { name: "Version", value: "0.0.1", inline: true },
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
