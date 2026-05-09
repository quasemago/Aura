import { Type } from "class-transformer";
import {
  IsArray,
  IsBoolean,
  IsInt,
  IsNumber,
  IsOptional,
  IsString,
  ValidateNested
} from "class-validator";

export class AnimeSearchResponseDTO {
  @ValidateNested()
  @Type(() => Pagination)
  pagination?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Anime)
  data?: Anime[];

  @IsString()
  createdBy?: string;
}

export class Pagination {
  @IsInt()
  last_visible_page!: number;

  @IsBoolean()
  has_next_page!: boolean;

  @IsInt()
  current_page!: number;

  @ValidateNested()
  @Type(() => Items)
  items!: Items;
}

export class Items {
  @IsInt()
  count!: number;

  @IsInt()
  total!: number;

  @IsInt()
  per_page!: number;
}

export class Anime {
  @IsInt()
  mal_id!: number;

  @IsString()
  url?: string;

  @ValidateNested()
  @Type(() => Images)
  images?: Images;

  @ValidateNested()
  @Type(() => Trailer)
  trailer?: Trailer;

  @IsBoolean()
  approved?: boolean;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Title)
  titles?: Title[];

  @IsString()
  title?: string;

  @IsString()
  title_english?: string;

  @IsString()
  title_japanese?: string;

  @IsArray()
  title_synonyms?: string[];

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  source: string = "N/A";

  @IsInt()
  episodes?: number;

  @IsString()
  @IsOptional()
  status: string = "N/A";

  @IsBoolean()
  airing?: boolean;

  @ValidateNested()
  @Type(() => Aired)
  aired?: Aired;

  @IsString()
  @IsOptional()
  duration: string = "N/A";

  @IsString()
  @IsOptional()
  rating: string = "N/A";

  @IsNumber()
  score?: number;

  @IsInt()
  scored_by?: number;

  @IsInt()
  rank?: number;

  @IsInt()
  popularity?: number;

  @IsInt()
  members?: number;

  @IsInt()
  favorites?: number;

  @IsString()
  @IsOptional()
  synopsis: string = "N/A";

  @IsString()
  @IsOptional()
  background: string = "N/A";

  @IsString()
  @IsOptional()
  season: string = "N/A";

  @IsInt()
  year?: number;

  @ValidateNested()
  @Type(() => Broadcast)
  broadcast?: Broadcast;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Producer)
  producers?: Producer[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Producer)
  licensors?: Producer[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Producer)
  studios?: Producer[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Genre)
  genres?: Genre[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Theme)
  themes?: Theme[];

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Demographic)
  demographics?: Demographic[];
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

  @IsString()
  small_image_url?: string;

  @IsString()
  large_image_url?: string;
}

export class Trailer {
  @IsString()
  @IsOptional()
  youtube_id: string = "N/A";

  @IsString()
  url?: string;

  @IsString()
  embed_url?: string;

  @ValidateNested()
  @Type(() => TrailerImages)
  images?: TrailerImages;
}

export class TrailerImages {
  @IsString() image_url?: string;
  @IsString() small_image_url?: string;
  @IsString() medium_image_url?: string;
  @IsString() large_image_url?: string;
  @IsString() maximum_image_url?: string;
}

export class Title {
  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  title: string = "N/A";
}

export class Aired {
  @IsString()
  @IsOptional()
  from: string = "N/A";

  @IsString()
  @IsOptional()
  to: string = "N/A";

  @ValidateNested()
  @Type(() => Prop)
  prop?: Prop;

  @IsString()
  @IsOptional()
  string: string = "N/A";
}

export class Prop {
  @ValidateNested()
  @Type(() => FromTo)
  from?: FromTo;

  @ValidateNested()
  @Type(() => FromTo)
  to?: FromTo;
}

export class FromTo {
  @IsInt() day?: number;
  @IsInt() month?: number;
  @IsInt() year?: number;
}

export class Broadcast {
  @IsString()
  @IsOptional()
  day: string = "N/A";

  @IsString()
  @IsOptional()
  time: string = "N/A";

  @IsString()
  @IsOptional()
  timezone: string = "N/A";

  @IsString()
  @IsOptional()
  string: string = "N/A";
}

export class Producer {
  @IsInt()
  @IsOptional()
  mal_id: number = 0;

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  name: string = "N/A";

  @IsString()
  url?: string;
}

export class Genre {
  @IsInt() mal_id?: number;

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  name: string = "N/A";

  @IsString()
  url?: string;
}

export class Theme {
  @IsInt() mal_id?: number;

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  name: string = "N/A";

  @IsString()
  url?: string;
}

export class Demographic {
  @IsInt() mal_id?: number;

  @IsString()
  @IsOptional()
  type: string = "N/A";

  @IsString()
  @IsOptional()
  name: string = "N/A";

  @IsString()
  url?: string;
}
