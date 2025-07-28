import { IsString, IsInt, IsOptional, IsNumber, IsArray, ValidateNested } from "class-validator";
import { Type } from "class-transformer";

export class MalSearchResponseDTO {
  @ValidateNested()
  @Type(() => UserProfile)
  data?: UserProfile;
}

export class UserProfile {
  @IsInt()
  mal_id!: number;

  @IsString()
  @IsOptional()
  username: string = "N/A";

  @IsString()
  url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  last_online: string = "N/A";

  @IsString()
  @IsOptional()
  gender: string = "N/A";

  @IsString()
  @IsOptional()
  birthday: string = "N/A";

  @IsString()
  @IsOptional()
  location: string = "N/A";

  @IsString()
  @IsOptional()
  joined: string = "N/A";

  @ValidateNested()
  @Type(() => Statistics)
  statistics?: Statistics;

  @ValidateNested()
  @Type(() => Favorites)
  favorites?: Favorites;

  @ValidateNested()
  @Type(() => Updates)
  updates?: Updates;

  @IsString()
  @IsOptional()
  about: string = "N/A";

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => ExternalLink)
  external?: ExternalLink[];
}

export class Images {
  @ValidateNested()
  @Type(() => ImageFormat)
  jpg?: ImageFormat;

  @ValidateNested()
  @Type(() => ImageFormat)
  webp?: ImageFormat;
}

export class ImageFormat {
  @IsString()
  image_url?: string;
}

export class Statistics {
  @ValidateNested()
  @Type(() => AnimeStats)
  anime?: AnimeStats;

  @ValidateNested()
  @Type(() => MangaStats)
  manga?: MangaStats;
}

export class AnimeStats {
  @IsNumber() days_watched?: number;
  @IsNumber() mean_score?: number;
  @IsInt() watching?: number;
  @IsInt() completed?: number;
  @IsInt() on_hold?: number;
  @IsInt() dropped?: number;
  @IsInt() plan_to_watch?: number;
  @IsInt() total_entries?: number;
  @IsInt() rewatched?: number;
  @IsInt() episodes_watched?: number;
}

export class MangaStats {
  @IsNumber() days_read?: number;
  @IsNumber() mean_score?: number;
  @IsInt() reading?: number;
  @IsInt() completed?: number;
  @IsInt() on_hold?: number;
  @IsInt() dropped?: number;
  @IsInt() plan_to_read?: number;
  @IsInt() total_entries?: number;
  @IsInt() reread?: number;
  @IsInt() chapters_read?: number;
  @IsInt() volumes_read?: number;
}

export class Favorites {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => FavoriteAnime)
  anime?: FavoriteAnime[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => FavoriteManga)
  manga?: FavoriteManga[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => FavoriteCharacter)
  characters?: FavoriteCharacter[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => FavoritePeople)
  people?: FavoritePeople[];
}

export class FavoriteAnime {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  title: string = "N/A";

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsInt()
  @IsOptional()
  start_year?: number;
}

export class FavoriteManga {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  title: string = "N/A";

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsInt()
  @IsOptional()
  start_year?: number;
}

export class FavoriteCharacter {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  name: string = "N/A";
}

export class FavoritePeople {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  name: string = "N/A";
}

export class Updates {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => AnimeUpdate)
  anime?: AnimeUpdate[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => MangaUpdate)
  manga?: MangaUpdate[];
}

export class AnimeUpdate {
  @ValidateNested()
  @Type(() => AnimeEntry)
  entry?: AnimeEntry;

  @IsNumber()
  score?: number;

  @IsString()
  @IsOptional()
  status: string = "N/A";

  @IsInt()
  episodes_seen?: number;

  @IsInt()
  episodes_total?: number;

  @IsString()
  @IsOptional()
  date: string = "N/A";
}

export class MangaUpdate {
  @ValidateNested()
  @Type(() => MangaEntry)
  entry?: MangaEntry;

  @IsNumber()
  score?: number;

  @IsString()
  @IsOptional()
  status: string = "N/A";

  @IsInt()
  chapters_read?: number;

  @IsInt()
  chapters_total?: number;

  @IsString()
  @IsOptional()
  date: string = "N/A";
}

export class AnimeEntry {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  title: string = "N/A";
}

export class MangaEntry {
  @IsInt() mal_id?: number;
  @IsString() url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @IsString()
  @IsOptional()
  title: string = "N/A";
}

export class ExternalLink {
  @IsString()
  @IsOptional()
  name: string = "N/A";

  @IsString()
  url?: string;
}
